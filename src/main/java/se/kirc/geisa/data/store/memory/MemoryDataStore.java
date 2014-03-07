/*
 * MemoryDataStore.java
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

package se.kirc.geisa.data.store.memory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import se.kirc.geisa.data.store.AbstractDataStore;
import se.kirc.geisa.data.store.DataStoreEntry;
import se.kirc.geisa.data.store.IndividualEntry;
import se.kirc.geisa.data.store.MarkerEntry;

public abstract class MemoryDataStore extends AbstractDataStore {
	private static final long serialVersionUID = 791817834483838L;
	protected byte[] genotypes;

	public MemoryDataStore(Collection<IndividualEntry> individuals,
			Collection<MarkerEntry> markers) {
		super(individuals, markers);
	}

	public DataStoreEntry getEntry(int index) throws IOException,
			IllegalStateException {
		// Get the genotype data.
		byte[] data = Arrays.copyOfRange(genotypes, entrySize * index,
				entrySize * (index + 1));

		return new DataStoreEntry(data, size, GENOTYPE_DENSITY);
	}

	public Iterator<DataStoreEntry> iterator() {
		return new EntryIterator(size);
	}

	private class EntryIterator implements Iterator<DataStoreEntry> {
		private int index;
		private int size;

		public EntryIterator(int size) {
			this.size = size;
			this.index = 0;
		}

		public boolean hasNext() {
			return index < size - 1;
		}

		public DataStoreEntry next() {
			if (index >= size)
				throw new NoSuchElementException();

			try {
				return getEntry(index++);
			} catch (IOException e) {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			// This is not implemented.
			throw new UnsupportedOperationException();
		}
	}
}
