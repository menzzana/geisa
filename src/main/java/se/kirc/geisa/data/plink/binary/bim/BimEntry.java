/*
 * BimEntry.java
 * Copyright (C) 2011-2012  KIRC
 * 
 * This file is part of JEIRA.
 * 
 * JEIRA is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * JEIRA is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package se.kirc.geisa.data.plink.binary.bim;

import java.io.Serializable;

import se.kirc.geisa.data.plink.Allele;

/**
 * This class represents an entry in a PLINK BIM file.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public class BimEntry implements Serializable {
	private static final long serialVersionUID = -7153361255060666169L;

	private String chromosome;
	private String id;
	private double geneticDistance;
	private long basePosition;
	private Allele firstAllele;
	private Allele secondAllele;

	/**
	 * Constructs a new BIM entry.
	 * 
	 * @param chromosome the chromosome number
	 * @param id the ID of the individual
	 * @param geneticDistance the marker's genetic distance
	 * @param basePosition the marker's base position
	 * @param firstAllele the first allele
	 * @param secondAllele the second allele
	 */
	public BimEntry(String chromosome, String id, double geneticDistance,
			long basePosition, Allele firstAllele, Allele secondAllele) {
		super();
		// Set all the private instance variables.
		this.chromosome = chromosome;
		this.id = id;
		this.geneticDistance = geneticDistance;
		this.basePosition = basePosition;
		this.firstAllele = firstAllele;
		this.secondAllele = secondAllele;
	}

	/**
	 * Returns the chromosome ID.
	 * 
	 * @return the chromosome ID
	 */
	public String getChromosome() {
		return chromosome;
	}

	/**
	 * Returns the individual's ID.
	 * 
	 * @return the individual's ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the genetic distance.
	 * 
	 * @return the genetic distance
	 */
	public double getGeneticDistance() {
		return geneticDistance;
	}

	/**
	 * Returns the base position.
	 * 
	 * @return the base position
	 */
	public long getBasePosition() {
		return basePosition;
	}

	/**
	 * Returns the first allele.
	 * 
	 * @return the first allele
	 */
	public Allele getFirstAllele() {
		return firstAllele;
	}

	/**
	 * Returns the second allele.
	 * 
	 * @return the second allele
	 */
	public Allele getSecondAllele() {
		return secondAllele;
	}
}
