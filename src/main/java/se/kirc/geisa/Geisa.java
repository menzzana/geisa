/*
 * Geisa.java
 * Copyright (C) 2011-2012  KIRC
 * 
 * This file is part of GEISA. An upgrade of the JEIRA
 * 
 * GEISA is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * GEISA is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.kirc.geisa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import se.kirc.geisa.conf.Configuration;
import se.kirc.geisa.data.plink.AbstractDataSet;
import se.kirc.geisa.data.plink.AffectionStatus;
import se.kirc.geisa.data.plink.DataSet;
import se.kirc.geisa.data.plink.DataSetReader;
import se.kirc.geisa.data.plink.DataSetReaderFactory;
import se.kirc.geisa.data.plink.Genotype;
import se.kirc.geisa.data.plink.Sex;
import se.kirc.geisa.data.plink.binary.BinaryDataSet;
import se.kirc.geisa.data.store.DataStore;
import se.kirc.geisa.data.store.DataStoreEntry;
import se.kirc.geisa.data.store.IndividualEntry;
import se.kirc.geisa.data.store.MarkerEntry;
import se.kirc.geisa.data.store.file.MarkerOrderedFileDataStore;
import se.kirc.geisa.data.store.memory.MarkerOrderedMemoryDataStore;

/**
 * The main class in the application (driver).
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */

public class Geisa {
	/**
	 * The configuration of the application execution.
	 */
	private Configuration configuration;

	/**
	 * Random number generator used for permutations.
	 */
	private Random random;

	/**
	 * The executor service (thread pool).
	 */
	private ExecutorService executor;

	/**
	 * Constructs a new Geisa class with the given configuration.
	 * 
	 * @param configuration
	 */
	private List<Double> APpLimits=new LinkedList<Double>();
	private List<Double> MULTLimits=new LinkedList<Double>();
	
	public Geisa(Configuration configuration) {
		// Store a reference to the configuration.
		this.configuration = configuration;

	}

	/**
	 * Get the {@link AbstractDataSet} based on the configuration.
	 * 
	 * @return an {@link AbstractDataSet} for the given configuration.
	 */
	private AbstractDataSet getDataSet() {
		AbstractDataSet result = null;

		// Get the interaction variable file.
		File interaction = configuration.getInteractionFile();

		// Check if we have a binary data set.
		if (configuration.getBinaryDataSet() != null) {
			String base = configuration.getBinaryDataSet();
			File bed = new File(base + ".bed");
			File bim = new File(base + ".bim");
			File fam = new File(base + ".fam");

			result = new BinaryDataSet(interaction, bed, bim, fam);
		}
		// TODO: Add for regular and transposed data sets.

		// Return the data set.
		return result;
	}

	/**
	 * Get the {@link DataStore} for a given {@link DataSet}.
	 * 
	 * @param dataSet
	 *            the data set.
	 * @return a data store.
	 * @throws IOException
	 *             if a read error occours.
	 */

	public String getPOMProjectVersion() throws IOException{
		final String POM_PROPERTIES_PATH="/META-INF/maven/se.kirc.geisa/geisa/pom.properties";
		
		InputStream stream = getClass().getResourceAsStream(POM_PROPERTIES_PATH);
		Properties props = new Properties();
		if (stream==null)
			return "Development";
		props.load(stream);
		return props.get("version").toString();
	}
	
	private List<String> getInteractionMarkers(DataSet dataSet) throws IOException {
		List<String> interactionMarkers;
		BufferedReader br;
		String s1;
		
		interactionMarkers=new ArrayList<String>();
		if (configuration.getMarkerFile()==null) {
			interactionMarkers.add(configuration.getInteractionFile().getName());
		}
		else {
			br=new BufferedReader(new FileReader(configuration.getMarkerFile()));
			while ((s1=br.readLine())!=null) {
				if (s1.trim().length()==0)
					continue;
				interactionMarkers.add(s1);
			}
			br.close();
		}
		return interactionMarkers;
	}
	
	private void getSignificanceLimits(File limitFile) throws IOException {
		StringTokenizer tokenizer;
		int i1,appCol,multCol;
		String s1;
		
		if (limitFile==null)
			return;		
		appCol=multCol=-1;
		BufferedReader br=new BufferedReader(new FileReader(limitFile));
		// Discard the header line.
		tokenizer=new StringTokenizer(br.readLine());
		// Check headers in file
		for (i1=0; tokenizer.hasMoreTokens(); i1++) {
			s1=tokenizer.nextToken();
			if (s1.equalsIgnoreCase("CUTOFF_APP"))
				appCol=i1;
			if (s1.equalsIgnoreCase("CUTOFF_MULT"))
				multCol=i1;
		}
		while ((s1=br.readLine())!=null) {
			if (s1.trim().length()==0)
				continue;
			tokenizer=new StringTokenizer(s1);
			for (i1=0; tokenizer.hasMoreTokens(); i1++) {
				s1=tokenizer.nextToken();
				if (i1==appCol)
					APpLimits.add(Double.parseDouble(s1));
				if (i1==multCol)
					MULTLimits.add(Double.parseDouble(s1));
			}
		}
		br.close();
	}
	
	private DataStore getDataStore(DataSet dataSet) throws IOException {
		DataStore store = null;

		// Get the corresponding data set reader, then load the individuals and
		// markers.
		DataSetReader reader = DataSetReaderFactory.createDataSetReader(dataSet);
		Collection<IndividualEntry> individuals = reader.loadIndividuals();
		Collection<MarkerEntry> markers = reader.loadMarkers();

		// Create a data store based on our preferences. We always use marker
		// ordered data stores.
		switch (configuration.getDataStoreType()) {
			case FILE:
				store = new MarkerOrderedFileDataStore(individuals, markers);
				break;
			default:
				store = new MarkerOrderedMemoryDataStore(individuals, markers);
		}

		// Populate the data store with interaction data and genotypes.
		reader.loadInteractionData(store);
		reader.loadGenotypes(store);

		return store;
	}

	/**
	 * Get the correct result consumer as specified by the configuration.
	 * 
	 * @param done
	 *            AtomicBoolean signaling when the execution is done.
	 * @return a {@link Runnable} result consumer.
	 */
	private Runnable getResultConsumer(AtomicBoolean done,
			BlockingQueue<Future<Iterable<Map<ResultColumn, String>>>> queue,int noMarkers) {
		Runnable consumer;

		// create a file output result consumer which splits each
		// permutation into its own file.
		consumer = new FileOutputResultConsumer(done,
				configuration.getColumns(),configuration.getOutputDirectory(),
				configuration.getPermutations(),configuration.getPermutationOutput(),
				configuration.getTotalPermutationOutput(),
				configuration.getNegativeAPP(),APpLimits,MULTLimits,noMarkers,queue);
		return consumer;
	}
	
	private boolean DeleteResultFile(String file1) throws IOException {
		boolean fileExists;
		
		File f1=new File(configuration.getOutputDirectory().getPath()+File.separator+file1);
		fileExists=f1.exists();
		if (fileExists)
			if (!f1.delete())
				throw new IOException(String.format(Messages.getString("options.error.nodelete"),f1.getName()));
		return fileExists;
	}
	/**
	 * The main method of this class.
	 * 
	 * @throws IOException
	 *             if an I/O error occours.
	 * @throws InterruptedException
	 *             if the program is interrupted.
	 */
	public void run() throws IOException, InterruptedException {
		AtomicBoolean done = new AtomicBoolean(false);

		// Print some information message.
		System.err.println("Geisa version: "+getPOMProjectVersion());
		System.err.println(Messages.getString("info.header"));
		System.err.println(String.format(Messages.getString("info.header.datastore"),configuration.getDataStoreType().toString()));
		System.err.println(String.format(Messages.getString("info.header.filebase"),configuration.getDataSet()));
		System.err.println(String.format(Messages.getString("info.header.interaction"),configuration.getInteractionFileText()));
		System.err.println(String.format(Messages.getString("info.header.marker"),configuration.getMarkerFileText()));
		System.err.println(String.format(Messages.getString("info.header.limit"),configuration.getLimitFileText()));
		System.err.println(String.format(Messages.getString("info.header.outputdir"),configuration.getOutputDirectory()));
		System.err.println(String.format(Messages.getString("info.header.permutations"),configuration.getPermutations()));
		System.err.println(String.format(Messages.getString("info.header.modeltype"),configuration.getModelType().toString()));
		System.err.println(String.format(Messages.getString("info.header.seed"),configuration.getSeed()));
		System.err.println(String.format(Messages.getString("info.header.threads"),configuration.getWorkers()));
		System.err.println(String.format(Messages.getString("info.header.cutoff"),configuration.getCutOff()));
		System.err.println(String.format(Messages.getString("info.header.iteration"),configuration.getIteration()));
		System.err.println(String.format(Messages.getString("info.header.threshold"),configuration.getThreshold()));
		
		// Deleting previous result files
		DeleteResultFile("results.txt");
		DeleteResultFile("marker_permutation_results.txt");
		DeleteResultFile("total_permutation_results.txt");
		DeleteResultFile("total_permutations.txt");
		for (int i1=1; DeleteResultFile("results_permutation_"+i1+".txt"); i1++);

		// Print information to parameter file
		PrintStream out = new PrintStream(configuration.getOutputDirectory().getPath()+File.separator+
				"parameters.txt");
		out.println(String.format(Messages.getString("info.header.filebase"),configuration.getDataSet()));
		out.println(String.format(Messages.getString("info.header.interaction"),configuration.getInteractionFileText()));
		out.println(String.format(Messages.getString("info.header.marker"),configuration.getMarkerFileText()));
		out.println(String.format(Messages.getString("info.header.limit"),configuration.getLimitFileText()));
		out.println(String.format(Messages.getString("info.header.permutations"),configuration.getPermutations()));
		out.println(String.format(Messages.getString("info.header.modeltype"),configuration.getModelType().toString()));
		out.println(String.format(Messages.getString("info.header.seed"),configuration.getSeed()));
		out.println(String.format(Messages.getString("info.header.threads"),configuration.getWorkers()));
		out.println(String.format(Messages.getString("info.header.cutoff"),configuration.getCutOff()));
		out.println(String.format(Messages.getString("info.header.iteration"),configuration.getIteration()));
		out.println(String.format(Messages.getString("info.header.threshold"),configuration.getThreshold()));
		out.close();
		// We assume that the configuration is sane. It should've passed
		// validation by now. Get the data set and all interactionmarkers
		AbstractDataSet dataSet = getDataSet();
		DataStore dataStore = getDataStore(dataSet);
		if (!dataStore.isVariablePresentInInteractionFile() && configuration.getMarkerFile()==null)
			throw new IOException(String.format(Messages.getString("options.error.missinginteractionvariables")));		
					
		List<String> interactionMarkers=getInteractionMarkers(dataSet);
		getSignificanceLimits(configuration.getLimitFile());
		// Get the set of markers that are used in the analysis
		if (configuration.getMarkerFile()!=null) {
			List<String> allMarkers=new LinkedList<String>();
			for (MarkerEntry markerEntry : dataStore.getMarkers().values())
				allMarkers.add(markerEntry.getId());
			// Check wether interactionmarkers are present in the dataset
			for (String interactionMarker : interactionMarkers)
				if (!allMarkers.contains(interactionMarker))
					throw new IOException(String.format(Messages.getString("options.error.missinginteractionmarker"),interactionMarker));		
		}
		// Executor service with a fixed number of worker threads.
		executor = Executors.newFixedThreadPool(configuration.getWorkers() + 1);
		
		// Create a job queue.
		LinkedBlockingQueue<Future<Iterable<Map<ResultColumn, String>>>> queue = new LinkedBlockingQueue<Future<Iterable<Map<ResultColumn, String>>>>(
				configuration.getQueueSize());

		// Create a consumer and start it.
		Thread consumer = new Thread(getResultConsumer(done, queue,dataStore.getMarkers().values().size()));
		consumer.start();

		// Generate a list of affection statuses and interaction variables.
		List<AffectionStatus> originalAffectionStatus = new LinkedList<AffectionStatus>();
		List<Integer> interactionVariables = new LinkedList<Integer>();
		List<List<Float>> covariates = new LinkedList<List<Float>>();
		List<Sex> sexes = new LinkedList<Sex>();
		for (IndividualEntry entry : dataStore.getIndividuals().values()) {
				originalAffectionStatus.add(entry.getAffectionStatus());
				interactionVariables.add(entry.getInteractionVariable());
				sexes.add(entry.getSex());
				if (entry.getCovariate()!=null)
					if (entry.getCovariate().size()>0)
						covariates.add(entry.getCovariate());
		}
		ModelType modelType = configuration.getModelType();

		// Perform all permutations.
		int permutations = configuration.getPermutations();
		int cutoff = configuration.getCutOff();
		// Iterate through all unique interaction Markers
		for (String interactionMarker : interactionMarkers) {
			// Initiate random for every Interaction Marker
			random = new Random(configuration.getSeed());
			List<AffectionStatus> affectionStatus = new LinkedList<AffectionStatus>(originalAffectionStatus);
			
			// Empty all interaction variables if marker file present. Interaction from PED
			if (configuration.getMarkerFile()!=null) { 
				interactionVariables.clear();
				MarkerEntry markerEntry=dataStore.getMarkers().get(interactionMarker);
				int index=dataStore.getMarkerIndex(markerEntry);
				DataStoreEntry entry=dataStore.getEntry(index);
				Iterator<Genotype> genit = entry.iterator();
				Iterator<AffectionStatus> affit = affectionStatus.iterator();
				float[] alleles = new float[4];
				int offset;
				while (genit.hasNext()) {
					Genotype genotype = genit.next();
					AffectionStatus status = affit.next();	
					offset=(status==AffectionStatus.AFFECTED?Task.INDEX_CASE_PRIMARY:Task.INDEX_CONTROL_PRIMARY);
					switch (genotype) {
						case HOMOZYGOTE_PRIMARY:
							alleles[offset]+=2;
							break;
						case HOMOZYGOTE_SECONDARY:
							alleles[offset+1]+=2;
							break;			
						case HETEROZYGOTE:
							alleles[offset+1]++;
							alleles[offset]++;
							break;
					}
				}
				float ratioRiskAllelePrimary=alleles[Task.INDEX_CASE_PRIMARY]/alleles[Task.INDEX_CONTROL_PRIMARY];
				float ratioRiskAlleleSecondary=alleles[Task.INDEX_CASE_SECONDARY]/alleles[Task.INDEX_CONTROL_SECONDARY];
				Genotype riskHomozygoteGenotype=(ratioRiskAllelePrimary>ratioRiskAlleleSecondary?Genotype.HOMOZYGOTE_PRIMARY:Genotype.HOMOZYGOTE_SECONDARY);
				int interactionVariable;
				for (Genotype genotype : entry) {
					interactionVariable=-1;
					switch (genotype) {
						case HETEROZYGOTE:
							interactionVariable=(configuration.getModelType()==ModelType.DOMINANT?1:0);
							break;
						case HOMOZYGOTE_PRIMARY:
							interactionVariable=(riskHomozygoteGenotype==Genotype.HOMOZYGOTE_PRIMARY?1:0);
							break;
						case HOMOZYGOTE_SECONDARY:
							interactionVariable=(riskHomozygoteGenotype==Genotype.HOMOZYGOTE_SECONDARY?1:0);
							break;
					}
					interactionVariables.add(interactionVariable);
				}
			}
			String msg = String.format(Messages.getString("status.interactionmarker.begin"),interactionMarker);
			System.err.println(msg);
			
			for (int p = 0; p <= permutations; ++p) {
				String msg1;
				if (p==0)
					msg1 = String.format(
							Messages.getString("status.original.calculation.begin"),
							Calendar.getInstance());
				else
					msg1 = String.format(
						Messages.getString("status.iteration.begin"),
						Calendar.getInstance(), p, permutations);
				System.err.println(msg1);
	
				// List containing all task configurations to be performed per
				// batch.
				Iterator<MarkerEntry> it = dataStore.getMarkers().values().iterator();
				List<TaskConfiguration> configs = null;
	
				while (it.hasNext()) {
					configs = new LinkedList<TaskConfiguration>();
					// Split into batches.
					for (int i = 0; i < configuration.getBatchSize() && it.hasNext(); ++i) {
						MarkerEntry marker = it.next();
						int index = dataStore.getMarkerIndex(marker);
						DataStoreEntry entry = dataStore.getEntry(index);
						List<Integer>variables=new LinkedList<Integer>(interactionVariables);
						TaskConfiguration config = new TaskConfiguration(
								marker.getChromosome(), marker.getId(), interactionMarker, entry,
								sexes, affectionStatus, variables, covariates,
								marker.getFirstAllele(), marker.getSecondAllele(),
								p, cutoff, modelType,
								configuration.getIteration(),configuration.getThreshold());
	
						// Add the config to the list.
						configs.add(config);
					}
					if (configs.size()>0) {
						// Call the producer and add the future to both queues. If empty skip=No Selected markers
						Future<Iterable<Map<ResultColumn, String>>> future = executor.submit(new Task(configs));
						queue.put(future);
					}
				}
	
				// Randomize all the affection statuses.
				affectionStatus = new LinkedList<AffectionStatus>(affectionStatus);
				Collections.shuffle(affectionStatus, random);
			}
		}
		// Signal that the execution is complete.
		done.set(true);
		executor.shutdown();
	}
}
