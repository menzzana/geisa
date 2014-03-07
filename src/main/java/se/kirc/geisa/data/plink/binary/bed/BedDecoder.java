package se.kirc.geisa.data.plink.binary.bed;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BedDecoder {
	private BufferedInputStream input;
	private int size;
	private BedOrder mode;

	public final static int GENOTYPES_PER_BYTE = 4;
	
	/**
	 * Returns the byte size needed to hold a specified amount of genotypes.
	 * 
	 * @param count the amount of genotypes to hold
	 */
	public final static int getGenotypeSize(int count) {
		return (count - 1) / GENOTYPES_PER_BYTE + 1;
	}

	public BedDecoder(InputStream inputStream, int individualCount,
			int markerCount) throws IOException {
		this(new BufferedInputStream(inputStream), individualCount, markerCount);
	}

	public BedDecoder(BufferedInputStream input, int individualCount,
			int markerCount) throws IOException {
		this.input = input;

		// Read the header and verify the magic number.
		if (input.read() != 0x6c || input.read() != 0x1b)
			throw new IOException("Invalid header in BED file.");

		// Determine the mode of the file and calculate the number of bytes
		// needed for each mode.
		switch (input.read() & 0x1) {
		case 0:
			mode = BedOrder.INDIVIDUAL;
			size = getGenotypeSize(markerCount);
			break;
		default:
			mode = BedOrder.MARKER;
			size = getGenotypeSize(individualCount);
			break;
		}
	}
	
	public BedOrder getMode() {
		return mode;
	}

	public byte[] readEntry() throws IOException {
		byte data[] = new byte[size];

		// If there's nothing to read, return null.
		if (input.read(data) == -1)
			return null;
		else
			return data;
	}

}
