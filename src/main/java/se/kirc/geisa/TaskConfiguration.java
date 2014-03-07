/*
 * TaskConfiguration.java
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
package se.kirc.geisa;

import java.io.Serializable;
import java.util.List;

import se.kirc.geisa.data.plink.AffectionStatus;
import se.kirc.geisa.data.plink.Allele;
import se.kirc.geisa.data.plink.Genotype;
import se.kirc.geisa.data.plink.Sex;

/**
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class TaskConfiguration implements Serializable {
	private static final long serialVersionUID = -9107471386157450509L;

	/**
	 * An iterable containing all the genotypes.
	 */
	private Iterable<Genotype> genotypes;

	/**
	 * An iterable containing all the affection statuses (i.e. case/control).
	 */
	private Iterable<Sex> sexes;
	
	private Iterable<AffectionStatus> affectionStatus;

	/**
	 * An iterable containing all the interaction variables.
	 */
	private Iterable<Integer> interactionVariables;

	/**
	 * The primary allele.
	 */
	private Allele primaryAllele;

	/**
	 * The secondary allele.
	 */
	private Allele secondaryAllele;

	/**
	 * An iterable containing the covariates. Support for multiple covariates is
	 * a planned feature for a future release.
	 */
	private Iterable<List<Float>> covariates;

	/**
	 * The number of permutations to perform.
	 */
	private int permutation;

	/**
	 * The chromosome.
	 */
	private String chromosome;

	/**
	 * The name of the marker.
	 */
	private String marker;
	/**
	 * The name of the interaction marker.
	 */
	private String interactionMarker;
	/**
	 * The cut off value for each bin.
	 */
	private int cutoff;

	/**
	 * The model type used.
	 */
	private ModelType modelType;

	private int iteration;
	// the max number of logstic regression iterations
	
	private double threshold;
	// the min number of logstic regression stable threshold
	
	/**
	 * Constructs a new {@link TaskConfiguration} with the specified parameters.
	 * 
	 * @param chromosome
	 *            the name of the chromosome.
	 * @param marker
	 *            the name of the marker.
	 * @param interactionmarker
	 *            the name of the interactionmarker.
	 * @param genotypes
	 *            all the genotypes.
	 * @param affectionStatus
	 *            all the affection statuses.
	 * @param interactionVariables
	 *            all the interaction variables.
	 * @param covariates
	 *            all the covariates.
	 * @param primaryAllele
	 *            the primary {@link Allele}.
	 * @param secondaryAllele
	 *            the secondary {@link Allele}.
	 * @param permutation
	 *            the number of permutations to perform.
	 * @param cutoff
	 *            the cutoff value for the individual bins.
	 * @param modelType
	 *            the {@link ModelType} to use (e.g. DOMINANT).
	 */
	public TaskConfiguration(String chromosome, String marker, String interactionMarker,
			Iterable<Genotype> genotypes,
			Iterable<Sex> sexes,
			Iterable<AffectionStatus> affectionStatus,
			Iterable<Integer> interactionVariables,
			Iterable<List<Float>> covariates, Allele primaryAllele,
			Allele secondaryAllele, int permutation, int cutoff,
			ModelType modelType,
			int Iteration, double threshold) {
		// Set all the supplied parameters.
		this.chromosome = chromosome;
		this.marker = marker;
		this.interactionMarker=interactionMarker;
		this.genotypes = genotypes;
		this.sexes = sexes;
		this.affectionStatus = affectionStatus;
		this.primaryAllele = primaryAllele;
		this.secondaryAllele = secondaryAllele;
		this.interactionVariables = interactionVariables;
		this.covariates = covariates;
		this.permutation = permutation;
		this.cutoff = cutoff;
		this.modelType = modelType;
		this.iteration = Iteration;
		this.threshold = threshold;
	}

	/**
	 * Get all the genotypes.
	 * 
	 * @return all the genotypes.
	 */
	public Iterable<Genotype> getGenotypes() {
		return genotypes;
	}

	/**
	 * Get all the affection statuses.
	 * 
	 * @return the affection statuses.
	 */
	public Iterable<Sex> getSex() {
		return sexes;
	}
	
	public Iterable<AffectionStatus> getAffectionStatus() {
		return affectionStatus;
	}

	/**
	 * Get the interaction variables.
	 * 
	 * @return the interaction variables.
	 */
	public Iterable<Integer> getInteractionVariables() {
		return interactionVariables;
	}

	/**
	 * Get the primary allele.
	 * 
	 * @return the primary allele.
	 */
	public Allele getPrimaryAllele() {
		return primaryAllele;
	}

	/**
	 * Get the secondary allele.
	 * 
	 * @return the secondary allele.
	 */
	public Allele getSecondaryAllele() {
		return secondaryAllele;
	}

	/**
	 * Get all the covariates.
	 * 
	 * @return the covariates.
	 */
	public Iterable<List<Float>> getCovariates() {
		return covariates;
	}

	/**
	 * Get all the permutations.
	 * 
	 * @return all the permutations.
	 */
	public int getPermutation() {
		return permutation;
	}

	/**
	 * Get the chromosome.
	 * 
	 * @return the chromosome.
	 */
	public String getChromosome() {
		return chromosome;
	}

	/**
	 * Get the name of the marker.
	 * 
	 * @return the name of the marker.
	 */
	public String getMarker() {
		return marker;
	}
	/**
	 * Get the name of the marker.
	 * 
	 * @return the name of the marker.
	 */
	public String getInteractionMarker() {
		return interactionMarker;
	}
	/**
	 * Get the cut off.
	 * 
	 * @return the cut off.
	 */
	public int getCutOff() {
		return cutoff;
	}

	/**
	 * Get the model type.
	 * 
	 * @return the model type.
	 */
	public ModelType getModelType() {
		return modelType;
	}

	public boolean isDominantOrXMale(Sex sex) {
		if (modelType.equals(ModelType.DOMINANT))
			return true;
		if (sex==null)
			return false;
		return (chromosome.equalsIgnoreCase("X") && sex==Sex.MALE);
		}

	public int getIteration() {
		return iteration;
	}

	public double getThreshold() {
		return threshold;
	}

}
