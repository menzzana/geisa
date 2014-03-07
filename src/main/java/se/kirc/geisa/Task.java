package se.kirc.geisa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

import se.kirc.geisa.data.plink.AffectionStatus;
import se.kirc.geisa.data.plink.Allele;
import se.kirc.geisa.data.plink.Genotype;
import se.kirc.geisa.data.plink.Sex;
import se.kirc.math.analysis.ExpRealFunction;
import se.kirc.math.regression.LogisticRegression;
import se.kirc.math.regression.LogisticRegressionConfigurationBuilder;

/**
 * This class represents a single, atomic task to be performed.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class Task implements Callable<Iterable<Map<ResultColumn, String>>>, Serializable {
	private static final long serialVersionUID = -1590080731359773271L;

	private Iterable<TaskConfiguration> configurations;

	final static int INDEX_CONTROL_PRIMARY = 0;
	final static int INDEX_CONTROL_SECONDARY = 1;
	final static int INDEX_CASE_PRIMARY = 2;
	final static int INDEX_CASE_SECONDARY = 3;
	final static int INDEX_CONTROL_CASE_OFFSET = 1;

	private final static int MATRIX_INDEX_A1B0 = 0;
	private final static int MATRIX_INDEX_A0B1 = 1;
	private final static int MATRIX_INDEX_A1B1 = 2;
	private final static int MATRIX_INDEX_COV2 = 3;
	private int MATRIX_INDEX_A0B0 = 4;					// Should be last in x2 matrix. Set later when definint covariates
	
	private final static int MATRIX_INDEX_A1m = 0;
	private final static int MATRIX_INDEX_B1m = 1;
	private final static int MATRIX_INDEX_A1mB1m = 2;
	private final static int MATRIX_INDEX_COV1 = 3;

	private final static int MISSING_INTERACTION = -1;

	private final static ExpRealFunction expFunc = new ExpRealFunction();
	private final static ChiSquaredDistributionImpl chisq = new ChiSquaredDistributionImpl(1);

	/**
	 * Constructs a new computational task.
	 * 
	 * @param markers
	 *            an iterable object of markers.
	 */
	public Task(Iterable<TaskConfiguration> configurations) {
		// Store a reference to the configurations.
		this.configurations = configurations;
	}

	public Iterable<Map<ResultColumn, String>> call() throws Exception {
		// List of results.
		LinkedList<Map<ResultColumn, String>> results = new LinkedList<Map<ResultColumn, String>>();

		// Iterate through all task configurations that should be called.
		for (TaskConfiguration configuration : configurations) {
			int recode = 0;

			// Create a result map to hold all calculations.
			HashMap<ResultColumn, String> result = new HashMap<ResultColumn, String>();

			// Put some basic information.
			result.put(ResultColumn.PERM,
					String.valueOf(configuration.getPermutation()));
			result.put(ResultColumn.INTERACTION,
					String.valueOf(configuration.getInteractionMarker()));
			result.put(ResultColumn.CHR,
					String.valueOf(configuration.getChromosome()));
			result.put(ResultColumn.SNP, configuration.getMarker());

			// Get a reduced set of the current data set.
			DataSet dataSet = reduceDataSet(configuration.getGenotypes(),
					configuration.getInteractionVariables(),
					configuration.getSex(),
					configuration.getAffectionStatus(),
					configuration.getCovariates());

			List<Integer> interactions = dataSet.getInteractionVariables();
			List<Genotype> genotypes = dataSet.getGenotypes();
			List<Sex> sexes = dataSet.getSex();
			List<AffectionStatus> affectionStatus = dataSet.getAffectionStatus();
			List<List<Float>> covariates = dataSet.getCovariates();

			// Get the allele summary for this task.
			AlleleSummary alleleSummary = getAlleleSummary(configuration);
			result.put(ResultColumn.RISK, Character.toString(alleleSummary.getRiskAllele().getValue()));
			result.put(ResultColumn.MAJOR, Character.toString(alleleSummary.getMajorAllele().getValue()));
			result.put(ResultColumn.MINOR, Character.toString(alleleSummary.getMinorAllele().getValue()));
			int[] riskFactors = new int[interactions.size()];
			Genotype norisk;
			Genotype onerisk = Genotype.HETEROZYGOTE;
			Genotype tworisk;

			if (alleleSummary.isRiskAllele(configuration.getPrimaryAllele())) {
				norisk = Genotype.HOMOZYGOTE_SECONDARY;
				tworisk = Genotype.HOMOZYGOTE_PRIMARY;
			} 
			else {
				norisk = Genotype.HOMOZYGOTE_PRIMARY;
				tworisk = Genotype.HOMOZYGOTE_SECONDARY;
			}
			
			// Calculate the risk factors.
			calculateRiskFactors(sexes, affectionStatus, genotypes, interactions,
					riskFactors, configuration,
					norisk, onerisk, tworisk,
					recode);

			// Result matrices and response vectors.
			int covariate_length=0;
			if (covariates.size()>0)
				covariate_length=covariates.get(0).size();
			RealMatrix x1 = new Array2DRowRealMatrix(riskFactors.length, MATRIX_INDEX_COV1+covariate_length);
			//x2 matrix +1 for A0B0 Variable. MATRIX_INDEX set accordingly
			RealMatrix x2 = new Array2DRowRealMatrix(riskFactors.length, MATRIX_INDEX_COV2+covariate_length+1);
			MATRIX_INDEX_A0B0=MATRIX_INDEX_COV2+covariate_length;
			RealVector y1 = new ArrayRealVector(riskFactors.length);
			RealVector y2 = new ArrayRealVector(riskFactors.length);

			// Initialize all cells to NaN.
			for (int i = 0; i < x1.getRowDimension(); ++i)
				for (int j = 0; j < x1.getColumnDimension(); ++j)
					x1.setEntry(i, j, Double.NaN);
			for (int i = 0; i < x2.getRowDimension(); ++i)
				for (int j = 0; j < x2.getColumnDimension(); ++j)
					x2.setEntry(i, j, Double.NaN);

			// Populate the response vectors.
			int index = 0;

			for (AffectionStatus status : affectionStatus) {
				int n = status.getValue();
				double value;

				switch (n) {
				case 1:
				case 2:
					value = n - 1;
					break;
				default:
					value = Double.NaN;
				}

				y1.setEntry(index, value);
				y2.setEntry(index++, value);
			}

			// Populate the covariate column in the matrices.
			if (covariates.size()>0) {
				index = 0;
				int index_c;
	
				for (List<Float> covariate : covariates) {
					index_c=0;
					for (float covar : covariate) {
						x1.setEntry(index, MATRIX_INDEX_COV1+index_c, covar);
						x2.setEntry(index, MATRIX_INDEX_COV2+index_c, covar);
						index_c++;
						}
					index++;
					}
				}
			// Get the risk counts and populate the matrices.
			int risk[][][] = calculateRiskMatrix(interactions, affectionStatus,
					x1, x2, riskFactors);

			// Clean out the data set and get rid of NA entries.
			CleanDataSet d1;
			CleanDataSet d2 = cleanDataSet(new CleanDataSet(x2, y2));

			RealMatrix tmpx = d2.getX();
			tmpx = tmpx.getSubMatrix(0, tmpx.getRowDimension()-1, 0,tmpx.getColumnDimension()-2);

			// Perform the initial logistic regression.
			LogisticRegressionConfigurationBuilder builder = new LogisticRegressionConfigurationBuilder();
			
			LogisticRegression lr = new LogisticRegression(
					builder.withBeta(null)
							.withX(tmpx.transpose())
							.withY(d2.getY())
							.withConvergenceThreshold(configuration.getThreshold())
							.withIterations(configuration.getIteration())
							.build());
			// lr.fit();

			int cutoff = configuration.getCutOff();

			if (risk[0][0][0] <= cutoff || risk[0][0][1] <= cutoff
					|| risk[0][1][0] <= cutoff || risk[0][1][1] <= cutoff
					|| risk[1][0][0] <= cutoff || risk[1][0][1] <= cutoff
					|| risk[1][1][0] <= cutoff || risk[1][1][1] <= cutoff) {
				result.put(ResultColumn.MULT, "0.0");
				result.put(ResultColumn.ORMIO, "0.0");
				result.put(ResultColumn.ORMIOL, "0.0");
				result.put(ResultColumn.ORMIOH, "0.0");
				result.put(ResultColumn.ORMOI, "0.0");
				result.put(ResultColumn.ORMOIL, "0.0");
				result.put(ResultColumn.ORMOIH, "0.0");
				result.put(ResultColumn.ORMII, "0.0");
				result.put(ResultColumn.ORMIIL, "0.0");
				result.put(ResultColumn.ORMIIH, "0.0");
				result.put(ResultColumn.STABLELRM,"NA");
			} else {
				// Multiplicative Analysis recode should always be 0
				// Clean out the data set and get rid of NA entries.
				d1 = cleanDataSet(new CleanDataSet(x1, y1));
				
				LogisticRegression lr1 = new LogisticRegression(
						builder.withBeta(null)
								.withX(d1.getX().transpose())
								.withY(d1.getY())
								.withConvergenceThreshold(configuration.getThreshold())
								.withIterations(configuration.getIteration())
								.build());
				// lr1.fit();	
				result.put(ResultColumn.STABLELRM,lr1.isLRStable()?
						String.format(Messages.getString("status.iteration.converging.yes")):
						String.format(Messages.getString("status.iteration.converging.no")));
				
				RealVector z = lr1.getZ();
				Double imuli=Math.pow(z.getEntry(MATRIX_INDEX_A1mB1m + 1), 2);
				imuli = 1 - chisq.cumulativeProbability(imuli);
				result.put(ResultColumn.MULT, String.valueOf(imuli));	
				
				// OR for multiplicative model.
				RealVector betas = lr1.getBeta();
				RealVector stderr = lr1.getStandardError();
				RealVector oddsRatios = betas.map(expFunc);
				RealVector oddsRatiosLow = betas.add(stderr.mapMultiply(-1.96))
						.map(expFunc);
				RealVector oddsRatiosHigh = betas.add(stderr.mapMultiply(1.96))
						.map(expFunc);
				
				result.put(ResultColumn.ORMIO, String.valueOf(oddsRatios
						.getEntry(MATRIX_INDEX_A1m + 1)));
				result.put(ResultColumn.ORMIOL, String.valueOf(oddsRatiosLow
						.getEntry(MATRIX_INDEX_A1m + 1)));
				result.put(ResultColumn.ORMIOH, String.valueOf(oddsRatiosHigh
						.getEntry(MATRIX_INDEX_A1m + 1)));
				result.put(ResultColumn.ORMOI, String.valueOf(oddsRatios
						.getEntry(MATRIX_INDEX_B1m + 1)));
				result.put(ResultColumn.ORMOIL, String.valueOf(oddsRatiosLow
						.getEntry(MATRIX_INDEX_B1m + 1)));
				result.put(ResultColumn.ORMOIH, String.valueOf(oddsRatiosHigh
						.getEntry(MATRIX_INDEX_B1m + 1)));
				result.put(ResultColumn.ORMII, String.valueOf(oddsRatios
						.getEntry(MATRIX_INDEX_A1mB1m + 1)));
				result.put(ResultColumn.ORMIIL, String.valueOf(oddsRatiosLow
						.getEntry(MATRIX_INDEX_A1mB1m + 1)));
				result.put(ResultColumn.ORMIIH, String.valueOf(oddsRatiosHigh
						.getEntry(MATRIX_INDEX_A1mB1m + 1)));				
			}
			
			
			// Extract all the coefficients.
			RealVector coef = lr.getBeta();
			double cA1B0 = coef.getEntry(MATRIX_INDEX_A1B0 + 1);
			double cA0B1 = coef.getEntry(MATRIX_INDEX_A0B1 + 1);
			double cA1B1 = coef.getEntry(MATRIX_INDEX_A1B1 + 1);

			// Recalculate the risk alleles if necessary.
			if (cA1B0 < 0 && cA1B0 < cA0B1 && cA1B0 < cA1B1) {
				recode=1;
				calculateRiskFactors(sexes, affectionStatus, genotypes, interactions,
						riskFactors, configuration,
						norisk, onerisk, tworisk,
						recode);
				risk = calculateRiskMatrix(interactions, affectionStatus, x1,
						x2, riskFactors);
			} else if (cA0B1 < 0 && cA0B1 < cA1B0 && cA0B1 < cA1B1) {
				// Calculate the risk factors.
				recode=2;
				interactions = swapInteractions(interactions);
				calculateRiskFactors(sexes, affectionStatus, genotypes, interactions,
						riskFactors, configuration,
						norisk, onerisk, tworisk,
						recode);
				risk = calculateRiskMatrix(interactions, affectionStatus, x1,
						x2, riskFactors);
			} else if (cA1B1 < 0 && cA1B1 < cA1B0 && cA1B1 < cA0B1) {
				recode=3;
				// Calculate the risk factors.
				interactions = swapInteractions(interactions);
				calculateRiskFactors(sexes, affectionStatus, genotypes, interactions,
						riskFactors, configuration,
						norisk, onerisk, tworisk,
						recode);
				risk = calculateRiskMatrix(interactions, affectionStatus, x1,
						x2, riskFactors);
			}

			result.put(ResultColumn.IND00_1, String.valueOf(risk[0][0][1]));
			result.put(ResultColumn.IND00_0, String.valueOf(risk[0][0][0]));
			result.put(ResultColumn.IND10_1, String.valueOf(risk[1][0][1]));
			result.put(ResultColumn.IND10_0, String.valueOf(risk[1][0][0]));
			result.put(ResultColumn.IND01_1, String.valueOf(risk[0][1][1]));
			result.put(ResultColumn.IND01_0, String.valueOf(risk[0][1][0]));
			result.put(ResultColumn.IND11_1, String.valueOf(risk[1][1][1]));
			result.put(ResultColumn.IND11_0, String.valueOf(risk[1][1][0]));
			result.put(ResultColumn.RECODE, String.valueOf(recode));
			result.put(ResultColumn.THRESHOLD, String.valueOf(configuration.getThreshold()));

			// Returning 0-values if no minor allele present, if interaction
			// variables less than 10
			if (risk[0][0][0] <= cutoff || risk[0][0][1] <= cutoff
					|| risk[0][1][0] <= cutoff || risk[0][1][1] <= cutoff
					|| risk[1][0][0] <= cutoff || risk[1][0][1] <= cutoff
					|| risk[1][1][0] <= cutoff || risk[1][1][1] <= cutoff) {
				result.put(ResultColumn.ORIO, "0.0");
				result.put(ResultColumn.ORIOL, "0.0");
				result.put(ResultColumn.ORIOH, "0.0");
				result.put(ResultColumn.ORII, "0.0");
				result.put(ResultColumn.ORIIL, "0.0");
				result.put(ResultColumn.ORIIH, "0.0");
				result.put(ResultColumn.OROI, "0.0");
				result.put(ResultColumn.OROIL, "0.0");
				result.put(ResultColumn.OROIH, "0.0");
				result.put(ResultColumn.AP, "0.0");
				result.put(ResultColumn.APL, "0.0");
				result.put(ResultColumn.APH, "0.0");
				result.put(ResultColumn.APP, "0.0");
				result.put(ResultColumn.STABLELRA,"NA");
			} else {
				// Additive analysis
				// Clean out the data set and get rid of NA entries.
				d2 = cleanDataSet(new CleanDataSet(x2, y2));

				RealMatrix d2x = d2.getX();

				d2x = d2x.getSubMatrix(0, d2x.getRowDimension() - 1, 0,
						d2x.getColumnDimension() - 2);

				LogisticRegression lr2 = new LogisticRegression(
						builder.withBeta(null)
								.withX(d2x.transpose())
								.withY(d2.getY())
								.withConvergenceThreshold(configuration.getThreshold())
								.withIterations(configuration.getIteration())
								.build());
				// lr2.fit();
				result.put(ResultColumn.STABLELRA,lr2.isLRStable()?
						String.format(Messages.getString("status.iteration.converging.yes")):
						String.format(Messages.getString("status.iteration.converging.no")));
				RealMatrix cor = lr2.getVarianceCovariance();

				// Calculate odds ratios.
				RealVector betas = lr2.getBeta();
				RealVector stderr = lr2.getStandardError();
				RealVector oddsRatios = betas.map(expFunc);
				RealVector oddsRatiosLow = betas.add(stderr.mapMultiply(-1.96)).map(expFunc);
				RealVector oddsRatiosHigh = betas.add(stderr.mapMultiply(1.96)).map(expFunc);
				result.put(ResultColumn.ORIO, String.valueOf(oddsRatios
						.getEntry(MATRIX_INDEX_A1B0 + 1)));
				result.put(ResultColumn.ORIOL, String.valueOf(oddsRatiosLow
						.getEntry(MATRIX_INDEX_A1B0 + 1)));
				result.put(ResultColumn.ORIOH, String.valueOf(oddsRatiosHigh
						.getEntry(MATRIX_INDEX_A1B0 + 1)));
				result.put(ResultColumn.ORII, String.valueOf(oddsRatios
						.getEntry(MATRIX_INDEX_A1B1 + 1)));
				result.put(ResultColumn.ORIIL, String.valueOf(oddsRatiosLow
						.getEntry(MATRIX_INDEX_A1B1 + 1)));
				result.put(ResultColumn.ORIIH, String.valueOf(oddsRatiosHigh
						.getEntry(MATRIX_INDEX_A1B1 + 1)));
				result.put(ResultColumn.OROI, String.valueOf(oddsRatios
						.getEntry(MATRIX_INDEX_A0B1 + 1)));
				result.put(ResultColumn.OROIL, String.valueOf(oddsRatiosLow
						.getEntry(MATRIX_INDEX_A0B1 + 1)));
				result.put(ResultColumn.OROIH, String.valueOf(oddsRatiosHigh
						.getEntry(MATRIX_INDEX_A0B1 + 1)));
				
				// Calculate RERI, AP and 95% CI for one allele.
				// TODO: Ta bort magic numbers och lägg till nya kolumnen
				// (A0B0).
				double reri = oddsRatios.getEntry(MATRIX_INDEX_A1B1 + 1)
						- oddsRatios.getEntry(MATRIX_INDEX_A1B0 + 1)
						- oddsRatios.getEntry(MATRIX_INDEX_A0B1 + 1) + 1;
				double ha1 = -Math.exp(betas.getEntry(MATRIX_INDEX_A1B0 + 1)
						- betas.getEntry(MATRIX_INDEX_A1B1 + 1));
				double ha2 = -Math.exp(betas.getEntry(MATRIX_INDEX_A0B1 + 1)
						- betas.getEntry(MATRIX_INDEX_A1B1 + 1));
				double ha3 = (oddsRatios.getEntry(MATRIX_INDEX_A0B1 + 1)
						+ oddsRatios.getEntry(MATRIX_INDEX_A1B0 + 1) - 1)
						/ oddsRatios.getEntry(MATRIX_INDEX_A1B1 + 1);
				// (ORooi + ORioo - 1) / ORioi

				double corA0B1 = cor.getEntry(MATRIX_INDEX_A0B1 + 1,
						MATRIX_INDEX_A0B1 + 1);
				double corA1B0 = cor.getEntry(MATRIX_INDEX_A1B0 + 1,
						MATRIX_INDEX_A1B0 + 1);
				double corA1B1 = cor.getEntry(MATRIX_INDEX_A1B1 + 1,
						MATRIX_INDEX_A1B1 + 1);
				double cor12 = cor.getEntry(MATRIX_INDEX_A1B0 + 1,
						MATRIX_INDEX_A0B1 + 1);
				double cor13 = cor.getEntry(MATRIX_INDEX_A1B0 + 1,
						MATRIX_INDEX_A1B1 + 1);
				double cor23 = cor.getEntry(MATRIX_INDEX_A0B1 + 1,
						MATRIX_INDEX_A1B1 + 1);
				double SeAP = Math.sqrt(Math.pow(ha1, 2) * corA1B0
						+ Math.pow(ha2, 2) * corA0B1 + Math.pow(ha3, 2)
						* corA1B1 + 2 * ha1 * ha2 * cor12 + 2 * ha1 * ha3
						* cor23 + 2 * ha2 * ha3 * cor13);
				result.put(
						ResultColumn.AP,
						String.valueOf(reri
								/ oddsRatios.getEntry(MATRIX_INDEX_A1B1 + 1)));
				result.put(
						ResultColumn.APL,
						String.valueOf(reri
								/ oddsRatios.getEntry(MATRIX_INDEX_A1B1 + 1)
								- 1.96 * SeAP));
				result.put(
						ResultColumn.APH,
						String.valueOf(reri
								/ oddsRatios.getEntry(MATRIX_INDEX_A1B1 + 1)
								+ 1.96 * SeAP));
				NormalDistributionImpl nd = new NormalDistributionImpl(0, SeAP);
				result.put(
						ResultColumn.APP,
						String.valueOf((1 - nd.cumulativeProbability(Math.abs(reri
								/ oddsRatios.getEntry(MATRIX_INDEX_A1B1 + 1)))) * 2));

			}

			// Add the result.
			results.add(result);
		}

		return results;
	}

	private static LinkedList<Integer> swapInteractions(
			Iterable<Integer> interactions) {
		LinkedList<Integer> result = new LinkedList<Integer>();

		// Add all integers to the new list and convert 0 to 1 and all others to
		// 0.
		for (Integer i : interactions) {
			switch (i) {
			case -1:
				// Leave NA.
				result.add(-1);
				break;
			case 0:
				result.add(1);
				break;
			default:
				result.add(0);
			}
		}

		return result;
	}

	private static CleanDataSet cleanDataSet(CleanDataSet dataSet) {
		// Get the matrix and response vector.
		RealMatrix x = dataSet.getX();
		RealVector y = dataSet.getY();

		// Create arrays large enough to hold all entries.
		ArrayList<RealVector> rows = new ArrayList<RealVector>(x.getRowDimension());
		ArrayList<Double> vals = new ArrayList<Double>(y.getDimension());

		// Initialize index and row counter, iterate over all rows in the
		// matrix.
		for (int i = 0, row = -1; i < x.getRowDimension(); ++i) {
			// Iterate through all columns.
			for (int j = 0; j < x.getColumnDimension() && row != i; ++j) {
				// Look for uninitialized values.
				if (Double.compare(x.getEntry(i, j), Double.NaN) == 0)
					row = i;
			}

			if (row != i) {
				rows.add(x.getRowVector(row = i));
				vals.add(y.getEntry(row));
			}
		}

		// Construct a new matrix and response vector with the correct sizes.
		RealMatrix newX = new Array2DRowRealMatrix(rows.size(),x.getColumnDimension());
		RealVector newY = new ArrayRealVector(vals.size());

		// Add the rows and entries to the matrix/vector.
		for (int i = 0; i < rows.size(); ++i) {
			newX.setRowVector(i, rows.get(i));
			newY.setEntry(i, vals.get(i));
		}

		// Return the new dataset.
		return new CleanDataSet(newX, newY);
	}

	private final static AlleleSummary getAlleleSummary(
			TaskConfiguration configuration) {
		AlleleSummary summary = new AlleleSummary();
		Iterable<Genotype> genotypes = configuration.getGenotypes();
		Iterable<AffectionStatus> statuses = configuration.getAffectionStatus();

		// Array counting alleles where the indexing is as following: 0 - case
		// primary, 1 - case secondary, 2 - control primary, 3 - control
		// secondary.
		int[] alleles = new int[4];

		// Iterate over all genotypes.
		Iterator<Genotype> genit = genotypes.iterator();
		Iterator<AffectionStatus> affit = statuses.iterator();

		while (genit.hasNext()) {
			Genotype genotype = genit.next();
			AffectionStatus status = affit.next();
			int offset;

			// Determine index offset depending on affection status.
			switch (status) {
			case UNAFFECTED:
				offset = INDEX_CONTROL_PRIMARY;
				break;
			case AFFECTED:
				offset = INDEX_CASE_PRIMARY;
				break;
			default:
				// Missing status, disregard.
				continue;
			}

			switch (genotype) {
			case HOMOZYGOTE_PRIMARY:
				alleles[offset] += 2;
				break;
			case HETEROZYGOTE:
				alleles[offset]++;
				alleles[offset + INDEX_CONTROL_CASE_OFFSET]++;
				break;
			case HOMOZYGOTE_SECONDARY:
				alleles[offset + INDEX_CONTROL_CASE_OFFSET] += 2;
				break;
			default:
				break;
			}
		}

		// Determine the most frequent alleles among the controls and cases, as
		// well as count them.
		Allele controlMaxAllele;
		Allele caseMaxAllele;
		Allele caseMinAllele;

		int controlTotal = alleles[INDEX_CONTROL_PRIMARY]
				+ alleles[INDEX_CONTROL_SECONDARY];
		int caseTotal = alleles[INDEX_CASE_PRIMARY]
				+ alleles[INDEX_CASE_SECONDARY];

		int controlMax;
		int controlMin;
		int caseMax;
		int caseMin;

		// Determine the major allele amongst the controls.
		if (alleles[INDEX_CONTROL_PRIMARY] > alleles[INDEX_CONTROL_SECONDARY]) {
			controlMaxAllele = configuration.getPrimaryAllele();
			controlMax = alleles[INDEX_CONTROL_PRIMARY];
			controlMin = alleles[INDEX_CONTROL_SECONDARY];
		} else {
			controlMaxAllele = configuration.getSecondaryAllele();
			controlMax = alleles[INDEX_CONTROL_SECONDARY];
			controlMin = alleles[INDEX_CONTROL_PRIMARY];
		}

		// Determine the major allele amongst the cases.
		if (alleles[INDEX_CASE_PRIMARY] > alleles[INDEX_CASE_SECONDARY]) {
			caseMaxAllele = configuration.getPrimaryAllele();
			caseMinAllele = configuration.getSecondaryAllele();
			caseMax = alleles[INDEX_CASE_PRIMARY];
			caseMin = alleles[INDEX_CASE_SECONDARY];
		} else {
			caseMaxAllele = configuration.getSecondaryAllele();
			caseMinAllele = configuration.getPrimaryAllele();
			caseMax = alleles[INDEX_CASE_SECONDARY];
			caseMin = alleles[INDEX_CASE_PRIMARY];
		}

		// Calculate the various ratios.
		double controlMaxRatio = (double) controlMax / (double) controlTotal;
		double caseMaxRatio = (double) caseMax / (double) caseTotal;
		double controlMinRatio = (double) controlMin / (double) controlTotal;
		double caseMinRatio = (double) caseMin / (double) controlTotal;

		summary.setControlMajorRatio(controlMaxRatio);
		summary.setControlMinorRatio(controlMinRatio);
		summary.setCaseMajorRatio(caseMaxRatio);
		summary.setCaseMinorRatio(caseMinRatio);

		// Determine the risk allele.
		if (caseMaxRatio > controlMaxRatio
				&& caseMaxAllele.equals(controlMaxAllele))
			summary.setRiskAllele(caseMaxAllele);
		else
			summary.setRiskAllele(caseMinAllele);

		// Determine the major and minor alleles.
		int primaryCount = alleles[INDEX_CONTROL_PRIMARY]
				+ alleles[INDEX_CASE_PRIMARY];
		int secondaryCount = alleles[INDEX_CONTROL_SECONDARY]
				+ alleles[INDEX_CASE_SECONDARY];
		int totalCount = primaryCount + secondaryCount;

		if (primaryCount > secondaryCount) {
			summary.setMajorAllele(configuration.getPrimaryAllele());
			summary.setMinorAllele(configuration.getSecondaryAllele());
			summary.setMajorRatio((double) primaryCount / (double) totalCount);
			summary.setMinorRatio((double) secondaryCount / (double) totalCount);
		} else {
			summary.setMajorAllele(configuration.getSecondaryAllele());
			summary.setMinorAllele(configuration.getPrimaryAllele());
			summary.setMajorRatio((double) secondaryCount / (double) totalCount);
			summary.setMinorRatio((double) primaryCount / (double) totalCount);
		}

		return summary;
	}

	/**
	 * This method reduces the data set and removes all initial N/A values.
	 * 
	 * @return
	 */
	private final static DataSet reduceDataSet(Iterable<Genotype> genotypes,
			Iterable<Integer> interactionVariables,
			Iterable<Sex> sexes,
			Iterable<AffectionStatus> affectionStatus,
			Iterable<List<Float>> covariates) {
		// New lists.
		LinkedList<Genotype> ng = new LinkedList<Genotype>();
		LinkedList<Integer> niv = new LinkedList<Integer>();
		LinkedList<Sex> sex1 = new LinkedList<Sex>();
		LinkedList<AffectionStatus> nas = new LinkedList<AffectionStatus>();
		LinkedList<List<Float>> nc = new LinkedList<List<Float>>();

		// Iterators;
		Iterator<Genotype> git = genotypes.iterator();
		Iterator<Integer> ivit = interactionVariables.iterator();
		Iterator<Sex> sexit = sexes.iterator();
		Iterator<AffectionStatus> asit = affectionStatus.iterator();
		Iterator<List<Float>> cit = covariates.iterator();

		while (ivit.hasNext()) {
			// Only add "good" data.
			Genotype genotype = git.next();
			Integer interaction = ivit.next();
			Sex sex=sexit.next();
			AffectionStatus status = asit.next();
			List<Float> covariate;
			if (!cit.hasNext())
				covariate=null;
			else
				covariate = cit.next();

			if (!genotype.equals(Genotype.UNKNOWN)
					&& !status.equals(AffectionStatus.MISSING)
					&& interaction != MISSING_INTERACTION) {
				ng.add(genotype);
				niv.add(interaction);
				sex1.add(sex);
				nas.add(status);
				if (covariate!=null)
					nc.add(covariate);
			}
		}

		return new DataSet(ng, niv, sex1, nas, nc);
	}

	private final static class DataSet {
		private List<Genotype> genotypes;
		private List<Integer> interactionVariables;
		private List<Sex> sexes;
		private List<AffectionStatus> affectionStatus;
		private List<List<Float>> covariates;

		public DataSet(List<Genotype> genotypes,
				List<Integer> interactionVariables, List<Sex> sexes,
				List<AffectionStatus> affectionStatus, LinkedList<List<Float>> nc) {
			this.genotypes = genotypes;
			this.interactionVariables = interactionVariables;
			this.sexes = sexes;
			this.affectionStatus = affectionStatus;
			this.covariates = nc;
		}

		public List<Genotype> getGenotypes() {
			return genotypes;
		}

		public List<Integer> getInteractionVariables() {
			return interactionVariables;
		}

		public List<Sex> getSex() {
			return sexes;
		}
		
		public List<AffectionStatus> getAffectionStatus() {
			return affectionStatus;
		}

		public List<List<Float>> getCovariates() {
			return covariates;
		}
	}

	/**
	 * @author danuve
	 * 
	 */
	private final static class CleanDataSet {
		private RealMatrix x;
		private RealVector y;

		public CleanDataSet(RealMatrix x, RealVector y) {
			this.x = x;
			this.y = y;
		}

		public RealMatrix getX() {
			return x;
		}

		public RealVector getY() {
			return y;
		}
	}

	private int[][][] calculateRiskMatrix(Iterable<Integer> interactions,
			Iterable<AffectionStatus> affectionStatus, RealMatrix x1,
			RealMatrix x2, int[] riskFactors) {
		Iterator<Integer> ivit = interactions.iterator();
		Iterator<AffectionStatus> affit = affectionStatus.iterator();

		// Reset the columns.
		for (int i : new int[] { MATRIX_INDEX_A0B1, MATRIX_INDEX_A1B0,
				MATRIX_INDEX_A1B1, MATRIX_INDEX_A0B0 }) {
			for (int j = 0; j < x2.getRowDimension(); ++j)
				x2.setEntry(j, i, Double.NaN);
		}
		for (int i : new int[] { MATRIX_INDEX_A1m,MATRIX_INDEX_B1m,MATRIX_INDEX_A1mB1m }) {
			for (int j = 0; j < x1.getRowDimension(); ++j)
				x1.setEntry(j, i, Double.NaN);
		}		

		/**
		 * Three dimensional jagged array containing counters. The first
		 * dimension is the risk factor (i.e. 0, 1). The second dimension is the
		 * presence of an interaction variable (i.e. 0 if the interaction
		 * variable is 0, 1 if the variable is > 0). The third dimension is the
		 * affection status of the individual (0 or 1).
		 */
		int data[][][] = new int[2][2][2];

		// Iterate over all risk factors.
		for (int i = 0; i < riskFactors.length; ++i) {
			int interaction = ivit.next();
			int status = affit.next().getValue() - 1;

			// Skip missing risk factors.
			if (riskFactors[i] != -1) {
				if (riskFactors[i] == 0 && interaction == 0) {
					x2.setEntry(i, MATRIX_INDEX_A0B0, 1);
					data[0][0][status]++;
				} else
					x2.setEntry(i, MATRIX_INDEX_A0B0, 0);
				if (riskFactors[i] == 1 && interaction == 0) {
					x2.setEntry(i, MATRIX_INDEX_A1B0, 1);
					data[1][0][status]++;
				}
				else
					x2.setEntry(i, MATRIX_INDEX_A1B0, 0);

				if (riskFactors[i] == 0 && interaction >= 1) {
					x2.setEntry(i, MATRIX_INDEX_A0B1, 1);
					data[0][1][status]++;
				}
				else
					x2.setEntry(i, MATRIX_INDEX_A0B1, 0);
				if (interaction >= 1 && riskFactors[i] == 1) {
					x2.setEntry(i, MATRIX_INDEX_A1B1, 1);
					data[1][1][status]++;
				}
				else
					x2.setEntry(i, MATRIX_INDEX_A1B1, 0);
				if (riskFactors[i] == 1)
					x1.setEntry(i, MATRIX_INDEX_A1m, 1);
				else
					x1.setEntry(i, MATRIX_INDEX_A1m, 0);
				if (interaction>0)
					x1.setEntry(i, MATRIX_INDEX_B1m, 1);
				else
					x1.setEntry(i, MATRIX_INDEX_B1m, 0);
				if (x1.getEntry(i, MATRIX_INDEX_A1m) != -1
						&& x1.getEntry(i, MATRIX_INDEX_B1m) != -1)
					x1.setEntry(
							i,
							MATRIX_INDEX_A1mB1m,
							x1.getEntry(i, MATRIX_INDEX_A1m)
									* x1.getEntry(i, MATRIX_INDEX_B1m));
			}
		}

		return data;
	}

	private Genotype[] GenotypeRisk(boolean isDominantOrXMaleMarker, 
			Genotype norisk, Genotype onerisk, Genotype tworisk, boolean unevenRecode) {
		Genotype[] risks;
		
		if (isDominantOrXMaleMarker)
			risks = (unevenRecode?new Genotype[] { norisk }:new Genotype[] { onerisk, tworisk });
		else
			risks = (unevenRecode?new Genotype[] { norisk, onerisk }:new Genotype[] { tworisk });
		return risks;
		}

	private Genotype[] GenotypeNoRisk(boolean isDominantOrXMaleMarker, 
			Genotype norisk, Genotype onerisk, Genotype tworisk, boolean unevenRecode) {
		Genotype[] norisks;
		
		if (isDominantOrXMaleMarker)
			norisks = (unevenRecode?new Genotype[] { onerisk, tworisk}: new Genotype[] {  norisk });
		else
			norisks = (unevenRecode?new Genotype[] { tworisk }: new Genotype[] { norisk, onerisk });
		return norisks;
		}
	
	private void calculateRiskFactors(
			Iterable<Sex> sexes,
			Iterable<AffectionStatus> affectionStatus,
			Iterable<Genotype> genotypes, Iterable<Integer> interactions,
			int[] riskFactors, TaskConfiguration configuration,
			Genotype norisk, Genotype onerisk, Genotype tworisk, int recode) {
		
		// Get iterators.
		Iterator<Genotype> genit = genotypes.iterator();
		Iterator<Sex> sexit = sexes.iterator();
		Iterator<Integer> intit = interactions.iterator();
		boolean unevenRecode=(recode%2==1);

		for (int index = 0; index < riskFactors.length; ++index) {
			Genotype genotype = genit.next();
			Sex sex = sexit.next();
			if (intit.next() != MISSING_INTERACTION) {
				int value = Integer.MIN_VALUE;
				Genotype norisks[] = GenotypeNoRisk(configuration.isDominantOrXMale(sex),
													norisk, onerisk, tworisk, unevenRecode);
				for (Genotype g : norisks)
					if (genotype.equals(g))
						riskFactors[index] = value = 0;
				Genotype risks[] = GenotypeRisk(configuration.isDominantOrXMale(sex),
													norisk, onerisk, tworisk, unevenRecode);
				for (Genotype g : risks)
					if (genotype.equals(g))
						riskFactors[index] = value = 1;

				// If other risk, set -1.
				if (value == Integer.MIN_VALUE)
					riskFactors[index] = -1;
			} else {
				// Remove interactions
				riskFactors[index] = -1;
			}
		}
	}
}
