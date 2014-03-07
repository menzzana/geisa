package se.kirc.geisa.data.plink;

import java.io.IOException;
import java.util.Collection;

import se.kirc.geisa.data.store.DataStore;
import se.kirc.geisa.data.store.IndividualEntry;
import se.kirc.geisa.data.store.MarkerEntry;

public interface DataSetReader {
	/**
	 * Load all genotypes and populate the data store.
	 * 
	 * @param store
	 *            the data store to populate.
	 * @throws IOException
	 *             if a read error occours.
	 */
	void loadGenotypes(DataStore store) throws IOException;

	/**
	 * Load all individuals from the data set.
	 * 
	 * @return a collection of all individuals in the data set.
	 * @throws IOException
	 *             if a read error occours.
	 */
	Collection<IndividualEntry> loadIndividuals() throws IOException;

	/**
	 * Load all markers from the data set.
	 * 
	 * @return a collection of all markers in the data set.
	 * @throws IOException
	 *             if a read error occours.
	 */
	Collection<MarkerEntry> loadMarkers() throws IOException;

	/**
	 * Load all interaction variable data and covariates into the data store.
	 * 
	 * @param store
	 *            the data store to populate.
	 * @throws IOException
	 *             if a read error occours.
	 */
	void loadInteractionData(DataStore store) throws IOException;
}
