/*
 * ConfigurationValidator.java
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

import se.kirc.geisa.Messages;

/**
 * This class is used to validate program configurations.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class ConfigurationValidator {
	public static void validate(Configuration configuration)
			throws InvalidValueException {
		// We need one of the available data sets.
		// TODO: Add regular and transposed data sets as they become available.
		if (configuration.getBinaryDataSet() == null
		// && configuration.getRegularDataSet() == null
		// && configuration.getTransposedDataSet() == null
		)
			throw new InvalidValueException(
					Messages.getString("configuration.error.dataset.missing"));
		// TODO: Fail if multiple data sets are used.
	}
}
