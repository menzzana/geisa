package se.kirc.math.regression;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.DecompositionSolver;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.SingularValueDecompositionImpl;
import org.apache.commons.math.stat.StatUtils;

import se.kirc.math.analysis.AbsRealFunction;
import se.kirc.math.analysis.Log1pRealFunction;
import se.kirc.math.analysis.LogRealFunction;
import se.kirc.math.analysis.LogitRealFunction;

public class LogisticRegression {
	private RealVector beta;
	private int iterations;
	private double difference = 1;
	private double logLikelihood;
	private RealMatrix jacobian;
	private RealMatrix varianceCovariance;
	private RealVector standardError;
	private RealVector z;

	private final static LogitRealFunction logit = new LogitRealFunction();
	private final static LogRealFunction log = new LogRealFunction();
	private final static Log1pRealFunction log1p = new Log1pRealFunction();
	private final static AbsRealFunction abs = new AbsRealFunction();

	public LogisticRegression(LogisticRegressionConfiguration config)
			throws DimensionMismatchException, FunctionEvaluationException,
			IllegalArgumentException {
		// Extract configuration parameters.
		RealMatrix x = config.getX();
		RealVector y = config.getY();
		beta = config.getBeta();
		double convergenceThreshold = config.getConvergenceThreshold();

		// Dimension check. The column count in the matrix should be the same as
		// the dimension of the vector.
		if (x.getColumnDimension() != y.getDimension())
			throw new DimensionMismatchException("x and y should have the same dimensions.");

		// Start with empty beta values + intercept which is filled with zeroes.
		if (beta.getDimension() != x.getRowDimension() + 1)
			throw new DimensionMismatchException(
					"Supplied beta vector has wrong dimension ("
							+ beta.getDimension() + " instead of "
							+ (x.getRowDimension() + 1) + ").");

		// Generate new x matrix with one more row than the original.
		x = new Array2DRowRealMatrix(x.getRowDimension() + 1,x.getColumnDimension());
		x.setSubMatrix(config.getX().getData(), 1, 0);
		RealVector r = x.getRowVector(0);
		r.set(1);
		x.setRowVector(0, r);

		RealMatrix xt = x.transpose();
		RealVector ny = y.mapMultiply(-1);
		RealVector oldBeta;

		// Iterate as much as possible/necessary.
		for (iterations = 0; iterations < config.getIterations(); ++iterations) {
			// Store the old beta values.
			oldBeta = beta;

			// f(z) = e^z / (e^z + 1)
			RealVector p = xt.operate(beta).map(logit);
			RealVector np = p.mapMultiply(-1);

			// Calculate the log likelihood.
			logLikelihood = StatUtils.sum(y.ebeMultiply(
					p.map(log).add(ny.mapAdd(1).ebeMultiply(np.map(log1p)))).getData());

			// Score
			RealVector s = x.operate(y.subtract(p));

			// Information matrix
			RealMatrix j = new Array2DRowRealMatrix(x.getRowDimension(),x.getColumnDimension());
			RealVector pnp = p.ebeMultiply(np.mapAdd(1)); // p * (1 - p)

			// Per row multiplication.
			for (int a = 0; a < x.getRowDimension(); a++)
				j.setRowVector(a, x.getRowVector(a).ebeMultiply(pnp));

			jacobian = j.multiply(xt);

			DecompositionSolver solver = new LUDecompositionImpl(jacobian).getSolver();

			if (!solver.isNonSingular())
				solver = new SingularValueDecompositionImpl(jacobian).getSolver();
			
			varianceCovariance = solver.getInverse();

			// Add new beta values.
			beta = beta.add(varianceCovariance.operate(s));

//			if (difference >= StatUtils.sum(beta.subtract(oldBeta).map(abs)
//					.getData()))
//				System.err.println("No converge!");
//			else
//				System.err.println("Converge... "
//						+ StatUtils.sum(beta.subtract(oldBeta).map(abs)
//								.getData()));

			// Sum of absolute difference.
			difference = StatUtils.sum(beta.subtract(oldBeta).map(abs).getData());

			// If the difference is within the convergence threshold, break the
			// iteration.
			if (difference <= convergenceThreshold)
				break;
		}

		// Set the standard error.
		standardError = new ArrayRealVector(varianceCovariance.getRowDimension());

		for (int i = 0; i < standardError.getDimension(); ++i)
			standardError.setEntry(i,Math.sqrt(varianceCovariance.getEntry(i, i)));
		z = beta.ebeDivide(standardError);
	}

	public RealVector getZ() {
		return z;
	}

	public RealVector getBeta() {
		return beta;
	}

	public RealMatrix getJacobian() {
		return jacobian;
	}

	public double getLogLikelihood() {
		return logLikelihood;
	}

	public double getDifference() {
		return difference;
	}
	
	public boolean isLRStable() {
		return difference<=LogisticRegressionConfiguration.DEFAULT_CONVERGENCE_THRESHOLD;
	}

	public int getIterations() {
		return iterations;
	}

	public RealMatrix getVarianceCovariance() {
		return varianceCovariance;
	}

	public RealVector getStandardError() {
		return standardError;
	}
}
