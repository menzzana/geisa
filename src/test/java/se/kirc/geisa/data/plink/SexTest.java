/*
 * SexTest.java
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.kirc.geisa.data.plink.Sex;


/**
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 */
public class SexTest {

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.Sex#getValue()}.
	 */
	@Test
	public void testGetValue() {
		// Assert that the values for each sex is consistent with that of PLINK.
		assertEquals(Sex.UNKNOWN.getValue(), 0);
		assertEquals(Sex.MALE.getValue(), 1);
		assertEquals(Sex.FEMALE.getValue(), 2);
	}

	/**
	 * Test method for
	 * {@link se.kirc.geisa.data.plink.Sex#getByValue(java.lang.String)}.
	 */
	@Test
	public void testGetByValueString() {
		// Assert that the values for each sex is consistent with that of PLINK.
		assertEquals(Sex.getByValue("-1"), Sex.UNKNOWN);
		assertEquals(Sex.getByValue("0"), Sex.UNKNOWN);
		assertEquals(Sex.getByValue("1"), Sex.MALE);
		assertEquals(Sex.getByValue("2"), Sex.FEMALE);
	}

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.Sex#getByValue(int)}.
	 */
	@Test
	public void testGetByValueInt() {
		// Assert that the values for each sex is consistent with that of PLINK.
		assertEquals(Sex.getByValue(-1), Sex.UNKNOWN);
		assertEquals(Sex.getByValue(0), Sex.UNKNOWN);
		assertEquals(Sex.getByValue(1), Sex.MALE);
		assertEquals(Sex.getByValue(2), Sex.FEMALE);
	}

}
