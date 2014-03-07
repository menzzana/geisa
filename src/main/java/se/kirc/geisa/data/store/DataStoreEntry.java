package se.kirc.geisa.data.store;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import se.kirc.geisa.data.plink.Genotype;

/**
 * The DataStoreEntry contains the raw genotype data as represented in the
 * DataStore from where it is retreived. This is usually either in SNP-major
 * (i.e. all genotypes for all individuals for one marker) or individual major
 * (i.e. all genotypes for all markers for one individual) mode.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class DataStoreEntry implements Serializable, Iterable<Genotype> {
	private static final long serialVersionUID = -2759908506064483873L;

	private byte[] genotypes;
	private int size;
	private int density;

	public DataStoreEntry(byte[] genotypes, int size, int density) {
		this.genotypes = genotypes;
		this.size = size;
		this.density = density;
	}

	public int getSize() {
		return size;
	}

	public Genotype getGenotype(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();

		return Genotype.getByValue((genotypes[index / density] >> (index
				% density) * 2) & 0x3);
	}

	public Iterator<Genotype> iterator() {
		return new EntryIterator(genotypes, size);
	}

	private class EntryIterator implements Iterator<Genotype> {
		private byte[] genotypes;
		private int size;
		private int index;

		public EntryIterator(byte[] genotypes, int size) {
			this.genotypes = genotypes;
			this.size = size;
			this.index = 0;
		}

		public boolean hasNext() {
			return index < size;
		}

		public Genotype next() {
			if (index >= size)
				throw new NoSuchElementException();

			Genotype genotype = Genotype
					.getByValue((genotypes[index / density] >> (index % density) * 2) & 0x3);

			index++;

			return genotype;
		}

		public void remove() {
			// This is not implemented.
			throw new UnsupportedOperationException();
		}
	}
}
