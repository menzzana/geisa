/*
 * DefaultConfiguration.java
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

import java.util.List;

import se.kirc.geisa.ModelType;
import se.kirc.geisa.ResultColumn;
import se.kirc.geisa.data.store.DataStoreType;

/**
 * A class containing all configuration parameters for the application to run
 * with some default settings specified.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class DefaultConfiguration extends Configuration {
	
	/**
	 * Constructs a new configuration with the default values set.
	 */
	public DefaultConfiguration() {
		// Let the parent class initialize.
		super();

		// Try to set all default values.
		try {
			List<ResultColumn> columns = getColumns();

			// Set default parameters.
			setPermutations(0);
			setSeed(System.currentTimeMillis());
			setWorkers(Runtime.getRuntime().availableProcessors());
			setQueueSize(getWorkers() * 20);
			setDataStoreType(DataStoreType.MEMORY);
			setBatchSize(200);
			setCutOff(10);
			setModelType(ModelType.DOMINANT);

			// Print all columns by default.
			for (ResultColumn column : ResultColumn.values())
				columns.add(column);
		} catch (InvalidValueException e) {
			// Disregard any errors. There shouldn't be any. If there are, we've
			// screwed up in the section above... or somewhere else.
		}
	}
}
