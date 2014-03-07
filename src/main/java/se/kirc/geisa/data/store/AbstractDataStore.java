/*
 * AbstractDataStore.java
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

package se.kirc.geisa.data.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import se.kirc.geisa.data.plink.Genotype;

/**
 * LinkedHashMap<K, V> is used to ensure insertion order.
 * 
 * @author danuve
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public abstract class AbstractDataStore implements DataStore, Serializable {
	private static final long serialVersionUID = -7980637761721777310L;

	public static final int GENOTYPE_DENSITY = 4;

	protected Map<String, IndividualEntry> individuals;
	protected Map<String, MarkerEntry> markers;

	protected Map<IndividualEntry, Integer> individualIndices;
	protected Map<MarkerEntry, Integer> markerIndices;

	protected int entrySize;
	protected int size;
	protected boolean VariableInInteractionFile;

	/**
	 *
	 */
	protected AbstractDataStore(Collection<IndividualEntry> individuals,
			Collection<MarkerEntry> markers) {
		// Initialize the hash maps and index lists.
		this.individuals = new LinkedHashMap<String, IndividualEntry>();
		this.markers = new LinkedHashMap<String, MarkerEntry>();
		this.individualIndices = new HashMap<IndividualEntry, Integer>();
		this.markerIndices = new HashMap<MarkerEntry, Integer>();
		this.VariableInInteractionFile=false;
		
		// Add all the individuals.
		for (IndividualEntry entry : individuals) {
			this.individualIndices.put(entry, this.individuals.size());
			this.individuals.put(entry.getId(), entry);
		}
		
		// Add all the markers.
		for (MarkerEntry entry : markers) {
			this.markerIndices.put(entry, this.markers.size());
			this.markers.put(entry.getId(), entry);
		}
	}
	
	public Map<String, MarkerEntry> getMarkers() {
		return markers;
	}

	public Map<String, IndividualEntry> getIndividuals() {
		return individuals;
	}

	public void setGenotype(IndividualEntry individual, MarkerEntry marker,
			Genotype genotype) throws IOException {
		setGenotype(getIndividualIndex(individual), getMarkerIndex(marker),
				genotype);
	}

	public Genotype getGenotype(IndividualEntry individual, MarkerEntry marker)
			throws IOException {
		return getGenotype(getIndividualIndex(individual),
				getMarkerIndex(marker));
	}

	public int getIndividualIndex(IndividualEntry individual) {
		Integer index = individualIndices.get(individual);

		return index == null ? -1 : index;
	}

	public boolean isVariablePresentInInteractionFile() {
		return VariableInInteractionFile;
	}
	
	public void setVariablePresentInInteractionFile(boolean isENVColumnPresent) {
		this.VariableInInteractionFile=isENVColumnPresent;
	}
	
	public int getMarkerIndex(MarkerEntry marker) {
		Integer index = markerIndices.get(marker);

		return index == null ? -1 : index;
	}
}
