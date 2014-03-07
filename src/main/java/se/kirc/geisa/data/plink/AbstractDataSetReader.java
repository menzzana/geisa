package se.kirc.geisa.data.plink;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import se.kirc.geisa.data.interaction.InteractionDecoder;
import se.kirc.geisa.data.interaction.InteractionEntry;
import se.kirc.geisa.data.store.DataStore;
import se.kirc.geisa.data.store.IndividualEntry;

public abstract class AbstractDataSetReader implements DataSetReader {
	private AbstractDataSet dataSet;

	protected AbstractDataSetReader(AbstractDataSet dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * Load all interaction variable data and covariates into the data store.
	 * 
	 * @param store
	 *            the data store to populate.
	 * @throws IOException
	 *             if a read error occours.
	 */
	public void loadInteractionData(DataStore store) throws IOException {
		if (dataSet.getInteractionFile()==null)
			return;
		// Get the interaction variable reader and construct a decoder using it.
		FileInputStream in = new FileInputStream(dataSet.getInteractionFile());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		InteractionDecoder decoder = new InteractionDecoder(reader);
		InteractionEntry entry;
		store.setVariablePresentInInteractionFile(decoder.isENVColumnPresent());
		
		// Get the set of individuals from the data store.
		Map<String, IndividualEntry> individuals = store.getIndividuals();

		// Read all the entries.
		while ((entry = decoder.readEntry()) != null) {
			// Get the ID and individual.
			String id = entry.getIndividualId();
			IndividualEntry individual = individuals.get(id);

			// If the individual is missing, create a new one.
			if (individual == null)
				individuals.put(id, individual = new IndividualEntry(id));

			// Set the individual's data.
			individual.setCovariate(entry.getCovariance());
			individual.setInteractionVariable(entry.getInteractionVariable());
		}
	}
}
