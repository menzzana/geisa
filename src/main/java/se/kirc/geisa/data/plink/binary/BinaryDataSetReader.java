package se.kirc.geisa.data.plink.binary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import se.kirc.geisa.data.plink.AbstractDataSetReader;
import se.kirc.geisa.data.plink.Genotype;
import se.kirc.geisa.data.plink.binary.bed.BedDecoder;
import se.kirc.geisa.data.plink.binary.bim.BimDecoder;
import se.kirc.geisa.data.plink.binary.bim.BimEntry;
import se.kirc.geisa.data.plink.binary.fam.FamDecoder;
import se.kirc.geisa.data.plink.binary.fam.FamEntry;
import se.kirc.geisa.data.store.DataStore;
import se.kirc.geisa.data.store.IndividualEntry;
import se.kirc.geisa.data.store.MarkerEntry;

public class BinaryDataSetReader extends AbstractDataSetReader {
	private BinaryDataSet dataSet;

	/**
	 * Constructs a new BinaryDataSetReader.
	 * 
	 * @param dataSet
	 *            the BinaryDataSet to read.
	 * @throws IOException
	 *             if a read error occours
	 */
	public BinaryDataSetReader(BinaryDataSet dataSet) throws IOException {
		super(dataSet);
		this.dataSet = dataSet;
	}

	public Collection<IndividualEntry> loadIndividuals() throws IOException {
		LinkedList<IndividualEntry> result = new LinkedList<IndividualEntry>();

		// Get the FAM reader and construct a decoder using it.
		FileInputStream in = new FileInputStream(dataSet.getFAMFile());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		FamDecoder decoder = new FamDecoder(reader);
		FamEntry entry;

		// Read all the entries.
		while ((entry = decoder.readEntry()) != null) {
			// Construct a new individual entry and populate it.
			// TODO: Make the individual entry contain all PLINK data.
			IndividualEntry individual = new IndividualEntry(entry.getIndividualId());
			individual.setAffectionStatus(entry.getPhenotype());

			individual.setSex(entry.getSex());
			// Add the individual to the list.
			result.add(individual);
		}

		return result;
	}

	public Collection<MarkerEntry> loadMarkers() throws IOException {
		LinkedList<MarkerEntry> result = new LinkedList<MarkerEntry>();

		// Get the BIM reader and construct a decoder using it.
		FileInputStream in = new FileInputStream(dataSet.getBIMFile());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		BimDecoder decoder = new BimDecoder(reader);
		BimEntry entry;

		// Read all the entries.
		while ((entry = decoder.readEntry()) != null) {
			// Construct a new marker entry and populate it.
			// TODO: Make the marker entry contain all PLINK data.
			MarkerEntry marker = new MarkerEntry(entry.getId());

			// Set the marker's data.
			marker.setChromosome(entry.getChromosome());
			marker.setFirstAllele(entry.getFirstAllele());
			marker.setSecondAllele(entry.getSecondAllele());

			// Add the marker to the list.
			result.add(marker);
		}

		return result;
	}

	/**
	 * Load all genotypes and populate the data store.
	 * 
	 * @param store
	 *            the data store to populate.
	 * @throws IOException
	 *             if a read error occours.
	 */
	public void loadGenotypes(DataStore store) throws IOException {
		Map<String, IndividualEntry> individuals = store.getIndividuals();
		Map<String, MarkerEntry> markers = store.getMarkers();

		// Get the BED input stream and construct a decoder using it.
		FileInputStream in = new FileInputStream(dataSet.getBEDFile());
		BufferedInputStream input = new BufferedInputStream(in);
		BedDecoder decoder = new BedDecoder(input, individuals.size(),
				markers.size());
		BinaryParser parser;
		byte[] data;

		// Determine the mode of the file and construct the correct parser.
		switch (decoder.getMode()) {
		case INDIVIDUAL:
			parser = new IndividualOrderBinaryParser(store, markers);
			break;
		case MARKER:
			parser = new MarkerOrderBinaryParser(store, individuals);
			break;
		default:
			throw new IOException("Invalid BED mode: "
					+ decoder.getMode().toString());
		}

		// Read all the entries.
		int index = 0;

		while ((data = decoder.readEntry()) != null)
			parser.parse(data, index++);

	}

	private static abstract class BinaryParser {
		protected DataStore store;
		protected int density;

		public BinaryParser(DataStore store) {
			this.store = store;
			this.density = BedDecoder.GENOTYPES_PER_BYTE;
		}

		public abstract void parse(byte[] data, int index) throws IOException;
	}

	private static class IndividualOrderBinaryParser extends BinaryParser {
		private Map<String, MarkerEntry> markers;

		public IndividualOrderBinaryParser(DataStore store,
				Map<String, MarkerEntry> markers) {
			super(store);

			this.markers = markers;
		}

		@Override
		public void parse(byte[] data, int index) throws IOException {
			for (int pos = 0; pos < markers.size(); ++pos) {
				int genotype = (data[pos / density] >> (pos % density) * 2) & 0x3;

				store.setGenotype(index, pos, Genotype.getByValue(genotype));
			}
		}
	}

	private static class MarkerOrderBinaryParser extends BinaryParser {
		private Map<String, IndividualEntry> individuals;

		public MarkerOrderBinaryParser(DataStore store,
				Map<String, IndividualEntry> individuals) {
			super(store);

			this.individuals = individuals;
		}

		@Override
		public void parse(byte[] data, int index) throws IOException {
			for (int pos = 0; pos < individuals.size(); ++pos) {
				int genotype = (data[pos / density] >> (pos % density) * 2) & 0x3;
				
				store.setGenotype(pos, index, Genotype.getByValue(genotype));
			}
		}
	}
}
