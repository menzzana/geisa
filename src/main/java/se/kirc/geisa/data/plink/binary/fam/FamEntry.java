/*
 * FamEntry.java
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

package se.kirc.geisa.data.plink.binary.fam;

import java.io.Serializable;

import se.kirc.geisa.data.plink.Sex;

/**
 * This class represents an entry in a PLINK FAM file.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public class FamEntry implements Serializable {
	private static final long serialVersionUID = 6407940981113031528L;

	private String familyId;
	private String individualId;
	private String paternalId;
	private String maternalId;
	private Sex sex;
	private int phenotype;

	/**
	 * Constructs a new FAM entry.
	 * 
	 * @param familyId
	 *            the family's ID
	 * @param individualId
	 *            the individual's ID
	 * @param paternalId
	 *            the father's ID
	 * @param maternalId
	 *            the mother's ID
	 * @param sex
	 *            the sex of the individual
	 * @param phenotype
	 *            the affection status of the individual
	 */
	public FamEntry(String familyId, String individualId, String paternalId,
			String maternalId, Sex sex, int phenotype) {
		// Store references to all fields.
		this.familyId = familyId;
		this.individualId = individualId;
		this.paternalId = paternalId;
		this.maternalId = maternalId;
		this.sex = sex;
		this.phenotype = phenotype;
	}

	/**
	 * Returns the entry's family ID.
	 * 
	 * @return the entry's family ID
	 */
	public String getFamilyId() {
		return familyId;
	}

	/**
	 * Returns the entry's individual ID.
	 * 
	 * @return the entry's individual ID
	 */
	public String getIndividualId() {
		return individualId;
	}

	/**
	 * Returns the entry's paternal ID.
	 * 
	 * @return the entry's paternal ID
	 */
	public String getPaternalId() {
		return paternalId;
	}

	/**
	 * Returns the entry's maternal ID.
	 * 
	 * @return the entry's maternal ID
	 */
	public String getMaternalId() {
		return maternalId;
	}

	/**
	 * Returns the entry's sex.
	 * 
	 * @return the entry's sex
	 */
	public Sex getSex() {
		return sex;
	}

	/**
	 * Returns the entry's phenotype.
	 * 
	 * @return the entry's phenotype
	 */
	public int getPhenotype() {
		return phenotype;
	}
}