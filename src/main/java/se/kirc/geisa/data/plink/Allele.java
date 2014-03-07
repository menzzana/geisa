/*
 * Allele.java
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
 * This enum represents the various alleles as used in PLINK.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public enum Allele {
	UNKNOWN('0'), A('A'), C('C'), T('T'), G('G');

	private char value;

	Allele(char value) {
		this.value = value;
	}

	/**
	 * Returns the character representation of the allele.
	 * 
	 * @return the character representation of the allele
	 */
	public char getValue() {
		return value;
	}

	/**
	 * Convert a String value to an Allele. If the String value is not known,
	 * the return value will be UNKNOWN.
	 * 
	 * @param value
	 *            the String value to convert
	 * @return the corresponding Allele
	 */
	public static Allele getByValue(String value) {
		if (value.length() < 1)
			return Allele.UNKNOWN;
		else
			return getByValue(value.charAt(0));
	}

	/**
	 * Convert a character value to an Allele. If the character value is not
	 * known, the return value will be UNKNOWN.
	 * 
	 * @param value
	 *            the character value to convert
	 * @return the corresponding Allele
	 */
	public static Allele getByValue(char value) {
		// The default allele is unknown.
		Allele result = Allele.UNKNOWN;

		// Iterate through all known alleles.
		for (final Allele allele : EnumSet.allOf(Allele.class)) {
			// If the allele matches, save the result.
			if (allele.getValue() == value) {
				result = allele;
			}
		}

		// Return the correct allele.
		return result;
	}
}
