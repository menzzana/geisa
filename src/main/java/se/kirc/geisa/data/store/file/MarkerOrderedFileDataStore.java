package se.kirc.geisa.data.store.file;

import java.io.IOException;
import java.util.Collection;

import se.kirc.geisa.data.plink.Genotype;
import se.kirc.geisa.data.store.IndividualEntry;
import se.kirc.geisa.data.store.MarkerEntry;

/**
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class MarkerOrderedFileDataStore extends FileDataStore {
	private static final long serialVersionUID = 4825009183221944630L;

	public MarkerOrderedFileDataStore(Collection<IndividualEntry> individuals,
			Collection<MarkerEntry> markers) throws IOException {
		super(individuals, markers);
	}

	public void setGenotype(int individual, int marker, Genotype genotype)
			throws IOException {
		// Calculate the index of the genotype.
		int index = entrySize * marker + individual / GENOTYPE_DENSITY;

		// Seek to the index and write the genotype.
		file.seek(index);
		file.write(file.read()
				| (genotype.getValue() & 0x3) << (individual % GENOTYPE_DENSITY) * 2);

		// Calculate the entry size and amount of entries.
		entrySize = (individuals.size() - 1) / GENOTYPE_DENSITY + 1;
		size = individuals.size();
	}

	public Genotype getGenotype(int individual, int marker) throws IOException,
			IllegalStateException {
		// Calculate the index of the genotype.
		int index = entrySize * marker + individual / GENOTYPE_DENSITY;

		// Seek to the index and read the genotype.
		file.seek(index);
		return Genotype
				.getByValue((file.read() >> (individual % GENOTYPE_DENSITY) * 2) & 0x3);
	}
}
