package se.kirc.geisa.data.store;

import java.io.IOException;
import java.util.Map;

import se.kirc.geisa.data.plink.Genotype;

/**
 * 
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public interface DataStore extends Iterable<DataStoreEntry> {
	Map<String, IndividualEntry> getIndividuals();

	Map<String, MarkerEntry> getMarkers();

	void setGenotype(int individual, int marker, Genotype genotype)
			throws IOException;

	void setGenotype(IndividualEntry individual, MarkerEntry marker,
			Genotype genotype) throws IOException;

	Genotype getGenotype(int individual, int marker) throws IOException;

	Genotype getGenotype(IndividualEntry individual, MarkerEntry marker)
			throws IOException;

	int getIndividualIndex(IndividualEntry individual);

	int getMarkerIndex(MarkerEntry marker);

	DataStoreEntry getEntry(int index) throws IOException;
	
	public boolean isVariablePresentInInteractionFile();
	
	public void setVariablePresentInInteractionFile(boolean isENVColumnPresent);
}
