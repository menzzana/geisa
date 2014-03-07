/*
 * FileDataStore.java
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

package se.kirc.geisa.data.store.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import se.kirc.geisa.data.store.AbstractDataStore;
import se.kirc.geisa.data.store.DataStoreEntry;
import se.kirc.geisa.data.store.IndividualEntry;
import se.kirc.geisa.data.store.MarkerEntry;

public abstract class FileDataStore extends AbstractDataStore {
	private static final long serialVersionUID = -7646700457690510105L;
	protected RandomAccessFile file;

	public FileDataStore(Collection<IndividualEntry> individuals,
			Collection<MarkerEntry> markers) throws IOException {
		super(individuals, markers);

		// Create a temp file which is removed when the program exits.
		File tmp = File.createTempFile("jeira-cache", ".tmp");
		tmp.deleteOnExit();

		// Create a random access file that will be used for accessing data.
		file = new RandomAccessFile(tmp, "rw");
	}

	public DataStoreEntry getEntry(int index) throws IOException {
		// Get the genotype data.
		byte[] data = new byte[entrySize];

		// Read the data from the store.
		file.seek(entrySize * index);
		file.read(data);

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
