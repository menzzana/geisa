package se.kirc.geisa.data.store.memory;

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
public class IndividualOrderedMemoryDataStore extends MemoryDataStore {
	private static final long serialVersionUID = 5881483472615073157L;

	public IndividualOrderedMemoryDataStore(
			Collection<IndividualEntry> individuals,
			Collection<MarkerEntry> markers) {
		super(individuals, markers);

		// Calculate the entry size and create a byte array large enough.
		entrySize = (markers.size() - 1) / GENOTYPE_DENSITY + 1;
		size = markers.size();
		genotypes = new byte[entrySize * individuals.size()];
	}

	public void setGenotype(int individual, int marker, Genotype genotype) {
		// Calculate the index of the genotype.
		int index = entrySize * individual + marker / GENOTYPE_DENSITY;

		// Set the genotype data.
		genotypes[index] |= (genotype.getValue() & 0x3) << (marker % GENOTYPE_DENSITY) * 2;
	}

	public Genotype getGenotype(int individual, int marker) {
		// Calculate the index of the genotype.
		int index = entrySize * individual + marker / GENOTYPE_DENSITY;

		// Read the genotype.
		return Genotype
				.getByValue((genotypes[index] >> (marker % GENOTYPE_DENSITY) * 2) & 0x3);
	}
}
