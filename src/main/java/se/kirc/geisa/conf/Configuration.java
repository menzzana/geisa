/*
 * Configuration.java
 * Copyright (C) 2011-2012  KIRC
 * 
 * This file is part of GEISA. GEISA is an upgrade of Jeira by Daniel Uvehag
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
package se.kirc.geisa.conf;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;

import se.kirc.geisa.Messages;
import se.kirc.geisa.ModelType;
import se.kirc.geisa.ResultColumn;
import se.kirc.geisa.data.store.DataStoreType;
import se.kirc.math.regression.LogisticRegressionConfiguration;

/**
 * A class containing all configuration parameters for the application to run.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class Configuration {
	/**
	 * Seed used by the Random Number Generator.
	 */
	private long seed;

	/**
	 * The amount of worker threads to use.
	 */
	private int workers;

	/**
	 * The amount of permutations to perform.
	 */
	private int permutations;

	/**
	 * The amount of calculations to perform in each task.
	 */
	private int batchSize;

	/**
	 * The minimum number of individuals in a group.
	 */
	private int cutoff;

	/**
	 * The amount of slots in the job queue.
	 */
	private int queueSize;

	/**
	 * The data store type to use (e.g. memory or file based).
	 */
	private DataStoreType dataStoreType;

	/**
	 * The output directory where all the files are to be stored.
	 */
	private File outputDirectory;

	/**
	 * The interaction variable file.
	 */
	private File interactionFile;

	/**
	 * The base name of the binary data set.
	 */
	private String binaryDataSet;

	/**
	 * The model type to use.
	 */
	private ModelType modelType;

	/**
	 * The list of columns to output.
	 */
	private LinkedList<ResultColumn> columns;

	/**
	 * Constructs a new, empty configuration.
	 */
	private File markerFile=null;
	/**
	 * Marker file HZ
	 */
	private File limitFile=null;
	/**
	 * Limit file HZ
	 */
	private int iteration=LogisticRegressionConfiguration.DEFAULT_MAX_ITERATIONS;
	
	private double threshold=LogisticRegressionConfiguration.DEFAULT_CONVERGENCE_THRESHOLD;
	
	private boolean permutationOutput=false;
	
	private boolean totalPermutationOutput=false;
	
	private boolean negativeapp=false;
	
	public Configuration() {
		// Create an empty list for the columns.
		columns = new LinkedList<ResultColumn>();

		outputDirectory = null;
	}

	/**
	 * Returns the seed.
	 * 
	 * @return the seed.
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * Set the seed for the random number generator.
	 * 
	 * @param seed
	 *            the seed to set.
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}

	/**
	 * Returns the number of worker threads to use.
	 * 
	 * @return the number of worker threads.
	 */
	public int getWorkers() {
		return workers;
	}

	/**
	 * Set the number of worker threads to use.
	 * 
	 * @param workers
	 *            the number of workers.
	 * @throws InvalidValueException
	 *             if the number of workers is less than 1.
	 */
	public void setWorkers(int workers) throws InvalidValueException {
		if (workers > 0)
			this.workers = workers;
		else
			throw new InvalidValueException("workers < 1");
	}

	/**
	 * Returns the number of permutations to perform.
	 * 
	 * @return the number of permutations.
	 */
	public int getPermutations() {
		return permutations;
	}

	/**
	 * Set the number of permutations to perform.
	 * 
	 * @param permutations
	 *            the number of permutations.
	 * @throws InvalidValueException
	 *             if the number of permutations is less than 0.
	 */
	public void setPermutations(int permutations) throws InvalidValueException {
		if (permutations >= 0)
			this.permutations = permutations;
		else
			throw new InvalidValueException("permutations < 0");
	}

	/**
	 * Returns the batch size to use when submitting tasks.
	 * 
	 * @return the batch size.
	 */
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * Sets the batch size to use when submitting tasks.
	 * 
	 * @param batchSize
	 *            the size of each batch.
	 * @throws InvalidValueException
	 *             if the size is less than 0.
	 */
	public void setBatchSize(int batchSize) throws InvalidValueException {
		if (batchSize > 0)
			this.batchSize = batchSize;
		else
			throw new InvalidValueException("batchSize < 1");
	}

	/**
	 * Returns the cut off value. When dividing the individuals (cases and
	 * controls) into different categories (risk or no risk) this value is used
	 * to make sure all bins have at least this number of individuals. If not,
	 * not enough data is present and the analysis is not performed.
	 * 
	 * @return the cut off value.
	 */
	public int getCutOff() {
		return batchSize;
	}

	/**
	 * Set the cut off value.
	 * 
	 * @param cutoff
	 *            the cut off value.
	 * @throws InvalidValueException
	 *             if the cut off value is less than 0.
	 */
	public void setCutOff(int cutoff) throws InvalidValueException {
		if (cutoff >= 0)
			this.batchSize = cutoff;
		else
			throw new InvalidValueException("cutoff < 0");
	}

	/**
	 * Returns the data store type to use.
	 * 
	 * @return the data store type.
	 */
	public DataStoreType getDataStoreType() {
		return dataStoreType;
	}

	/**
	 * Sets the data store type.
	 * 
	 * @param dataStoreType
	 *            the data store type.
	 * @throws InvalidValueException
	 *             if the type is null (unknown).
	 */
	public void setDataStoreType(DataStoreType dataStoreType)
			throws InvalidValueException {
		if (dataStoreType == null)
			throw new InvalidValueException("dataStoreType == null");
		else
			this.dataStoreType = dataStoreType;
	}

	/**
	 * Returns the output directory for the result files.
	 * 
	 * @return the output directory.
	 */
	public File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Sets the output directory for the result files.
	 * 
	 * @param outputDirectory
	 *            the output directory.
	 * @throws InvalidValueException
	 *             if the specified directory is not a directory, or null.
	 */
	
	public void setOutputDirectory(File outputDirectory) throws InvalidValueException {
		int i1;
		String s1;
		Calendar cal;
		
		this.outputDirectory = outputDirectory;
		if (this.outputDirectory==null) {
			cal = Calendar.getInstance();
			s1=String.format(Messages.getString("options.outputdirectory"),cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE));
			for (i1=1;; i1++) {
				this.outputDirectory=new File(s1+i1);
				if (!this.outputDirectory.isDirectory())
					break;				
				}
			}
		if (this.outputDirectory.isDirectory())
			return;
		if (!this.outputDirectory.mkdir())
			throw new InvalidValueException(Messages.getString("options.error.missingoutput"));
	}
	
	/**
	 * Returns the size of the queue.
	 * 
	 * @return the size of the queue.
	 */
	public int getQueueSize() {
		return queueSize;
	}

	/**
	 * Sets the size of the queue.
	 * 
	 * @param queueSize
	 *            the size of the queue.
	 * @throws InvalidValueException
	 *             if the size of the queue is less than 1.
	 */
	public void setQueueSize(int queueSize) throws InvalidValueException {
		if (queueSize > 0)
			this.queueSize = queueSize;
		else
			throw new InvalidValueException("queueSize < 1");
	}

	/**
	 * Returns the list of columns to use in the output.
	 * 
	 * @return the list of columns.
	 */
	public LinkedList<ResultColumn> getColumns() {
		return columns;
	}

	/**
	 * Returns the base name of data set for this analysis.
	 * 
	 * @return the base name of the data set.
	 */
	public String getDataSet() {
		String result = null;

		if (binaryDataSet != null)
			result = binaryDataSet;

		return result;
	}

	/**
	 * Returns the base name of the binary data set specified.
	 * 
	 * @return the base name of the binary data set.
	 */
	public String getBinaryDataSet() {
		return binaryDataSet;
	}

	/**
	 * Set the base name of the binary data set.
	 * 
	 * @param binaryDataSet
	 *            the base name of the binary data set (i.e. without the
	 *            extensions).
	 * @throws InvalidValueException
	 *             if either of the files doesn't exists.
	 */
	public void setBinaryDataSet(String binaryDataSet)
			throws InvalidValueException {
		// Create file objects.
		File bed = new File(binaryDataSet + ".bed");
		File bim = new File(binaryDataSet + ".bim");
		File fam = new File(binaryDataSet + ".fam");

		// Make sure all three files exist.
		if (!bed.exists() || !bed.isFile())
			throw new InvalidValueException("!bed.exists() || !bed.isFile()");
		else if (!bim.exists() || !bim.isFile())
			throw new InvalidValueException("!bim.exists() || !bim.isFile()");
		else if (!fam.exists() || !fam.isFile())
			throw new InvalidValueException("!fam.exists() || !fam.isFile()");

		this.binaryDataSet = binaryDataSet;
	}

	/**
	 * Returns the interaction file.
	 * 
	 * @return the interaction file.
	 */
	public File getInteractionFile() {
		return interactionFile;
	}

	public String getInteractionFileText() {
		return interactionFile==null?
				String.format(Messages.getString("configuration.error.interactionfile.missing")):
				interactionFile.getName();
	}
	/**
	 * Set the interaction file.
	 * 
	 * @param interactionFile
	 *            the interaction file.
	 * @throws InvalidValueException
	 *             if the files does not exist, or is not a file.
	 */
	public void setInteractionFile(File interactionFile)
			throws InvalidValueException {
		// Make sure the file exists.
		if (!interactionFile.exists() || !interactionFile.isFile())
			throw new InvalidValueException(
					"!interactionFile.exists() || !interactionFile.isFile()");
		else
			this.interactionFile = interactionFile;
	}

	/**
	 * Get the cut off.
	 * 
	 * @return the cut off.
	 */
	public int getCutoff() {
		return cutoff;
	}

	/**
	 * Set the cut off.
	 * 
	 * @param cutoff
	 *            the cut off.
	 * @throws InvalidValueException
	 *             if the value is less than 0.
	 */
	public void setCutoff(int cutoff) throws InvalidValueException {
		if (cutoff >= 0)
			this.cutoff = cutoff;
		else
			throw new InvalidValueException("cutoff < 0");
	}

	/**
	 * Get the model type.
	 * 
	 * @return the model type.
	 */
	public ModelType getModelType() {
		return modelType;
	}

	/**
	 * Set the model type.
	 * 
	 * @param modelType
	 *            the model type to use.
	 * @throws InvalidValueException
	 *             if the model type is null (unknown).
	 */
	public void setModelType(ModelType modelType) throws InvalidValueException {
		if (modelType == null)
			throw new InvalidValueException("modelType == null");
		else
			this.modelType = modelType;
	}
	public File getMarkerFile() {
		return markerFile;
	}
	
	public String getMarkerFileText() {
		return markerFile==null?
				String.format(Messages.getString("configuration.markerfile.none")):
				String.format(Messages.getString("configuration.markerfile.present"),markerFile.getName());
	}

	/**
	 * Set the marker file.
	 * 
	 * @param Marker File
	 *            the interaction file.
	 * @throws InvalidValueException
	 *             if the files does not exist, or is not a file.
	 */
	public void setMarkerFile(File markerFile) throws InvalidValueException {
		// Make sure the file exists.
		if (!markerFile.exists() || !markerFile.isFile())
			throw new InvalidValueException("!markerFile.exists() || !markerFile.isFile()");
		else
			this.markerFile = markerFile;
	}
	
	public File getLimitFile() {
		return limitFile;
	}

	public String getLimitFileText() {
		return limitFile==null?
				String.format(Messages.getString("configuration.error.limitfile.missing")):
				limitFile.getName();
	}
	/**
	 * Set the limit file.
	 * 
	 * @param limit File
	 *            the interaction file.
	 * @throws InvalidValueException
	 *             if the files does not exist, or is not a file.
	 */
	public void setLimitFile(File limitFile) throws InvalidValueException {
		// Make sure the file exists.
		if (!limitFile.exists() || !limitFile.isFile())
			throw new InvalidValueException("!limitFile.exists() || !limitFile.isFile()");
		else
			this.limitFile = limitFile;
	}
	/**
	 * Get the cut off.
	 * 
	 * @return the cut off.
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * Set the cut off.
	 * 
	 * @param cutoff
	 *            the cut off.
	 * @throws InvalidValueException
	 *             if the value is less than 0.
	 */
	public void setIteration(int iteration) throws InvalidValueException {
		if (iteration > 0)
			this.iteration = iteration;
		else
			throw new InvalidValueException("iteration < 0");
	}
	public double getThreshold() {
		return threshold;
	}

	/**
	 * Set the cut off.
	 * 
	 * @param cutoff
	 *            the cut off.
	 * @throws InvalidValueException
	 *             if the value is less than 0.
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public boolean getPermutationOutput() {
		return permutationOutput;
	}

	public void setPermutationOutput(boolean permutationOutput) {
		this.permutationOutput = permutationOutput;
	}

	public boolean getTotalPermutationOutput() {
		return totalPermutationOutput;
	}

	public void setTotalPermutationOutput(boolean totalPermutationOutput) {
		this.totalPermutationOutput = totalPermutationOutput;
	}
	
	public void setNegativeAPP(boolean negativeapp) {
		this.negativeapp=negativeapp;
	}
	public boolean getNegativeAPP() {
		return negativeapp;
	}
}
