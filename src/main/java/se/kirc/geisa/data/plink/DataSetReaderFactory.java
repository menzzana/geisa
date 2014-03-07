package se.kirc.geisa.data.plink;

import java.io.IOException;

import se.kirc.geisa.data.plink.binary.BinaryDataSet;
import se.kirc.geisa.data.plink.binary.BinaryDataSetReader;


public class DataSetReaderFactory {
	public static DataSetReader createDataSetReader(DataSet dataSet) throws IOException {
		// Create the right data set reader depending on the type of data set.
		if (dataSet instanceof BinaryDataSet)
			return new BinaryDataSetReader((BinaryDataSet) dataSet);
		// TODO: Add support for other data set readers.

		// No reader available.
		return null;
	}
}
