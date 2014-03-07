/*
 * AffectionStatus.java
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

package se.kirc.geisa.data.plink;

import java.util.EnumSet;

/**
 * This enum represents the various affection statuses used in PLINK.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public enum AffectionStatus {
	MISSING(0), UNAFFECTED(1), AFFECTED(2);

	private int value;

	/**
	 * Constructs a new AffectionStatus with the corresponding integer
	 * representation.
	 * 
	 * @param value
	 *            the integer representation
	 */
	AffectionStatus(int value) {
		this.value = value;
	}

	/**
	 * Returns the integer representation of the affection status.
	 * 
	 * @return the integer representation of the affection status
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Convert a String value to an AffectionStatus. If the String value is not
	 * known, the return value will be MISSING.
	 * 
	 * @param value
	 *            the String value to convert
	 * @return the corresponding AffectionStatus
	 */
	public static AffectionStatus getByValue(String value) {
		return getByValue(Integer.parseInt(value));
	}

	/**
	 * Convert an integer value to an AffectionStatus. If the integer value is
	 * not known, the return value will be MISSING.
	 * 
	 * @param value
	 *            the integer value to convert
	 * @return the corresponding AffectionStatus
	 */
	public static AffectionStatus getByValue(int value) {
		// The default affection status is missing.
		AffectionStatus result = AffectionStatus.MISSING;

		// Iterate through all known affection statuses.
		for (final AffectionStatus phenotype : EnumSet
				.allOf(AffectionStatus.class)) {
			// If the affection status matches, save the result.
			if (phenotype.getValue() == value) {
				result = phenotype;
			}
		}

		// Return the correct affection status.
		return result;
	}
}
