/*
 * Sex.java
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
 * This enum represents the various sexes used in PLINK.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public enum Sex {
	UNKNOWN(0), MALE(1), FEMALE(2);

	private int value;

	Sex(int value) {
		this.value = value;
	}

	/**
	 * Returns the integer representation of the sex.
	 * 
	 * @return the integer representation of the sex
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Convert a String value to a Sex. If the String value is not known, the
	 * return value will be UNKNOWN.
	 * 
	 * @param value
	 *            the String value to convert
	 * @return the corresponding Sex
	 */
	public static Sex getByValue(String value) {
		return getByValue(Integer.parseInt(value));
	}

	/**
	 * Convert an integer value to a Sex. If the integer value is not known, the
	 * return value will be UNKNOWN.
	 * 
	 * @param value
	 *            the integer value to convert
	 * @return the corresponding Sex
	 */
	public static Sex getByValue(int value) {
		// The default sex is unknown.
		Sex result = Sex.UNKNOWN;

		// Iterate through all known sexes.
		for (final Sex sex : EnumSet.allOf(Sex.class)) {
			// If the sex matches, save the result.
			if (sex.getValue() == value) {
				result = sex;
			}
		}

		// Return the correct sex.
		return result;
	}
}
