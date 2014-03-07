/**
 * 
 */
package se.kirc.geisa.data.plink;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import se.kirc.geisa.data.plink.binary.BinaryDataSet;
import se.kirc.geisa.data.plink.binary.BinaryDataSetReader;
import se.kirc.geisa.data.store.DataStore;
import se.kirc.geisa.data.store.IndividualEntry;

/**
 * @author danuve
 * 
 */
public class BinaryDataSetReaderTest {
	private DataStore store;
	private BinaryDataSet dataSet;
	private BinaryDataSetReader reader;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Set up files for the data set
		File interactionFile = new File("./src/test/resources/test.txt");
		File bedFile = new File("./src/test/resources/test.bed");
		File bimFile = new File("./src/test/resources/test.bim");
		File famFile = new File("./src/test/resources/test.fam");

		dataSet = new BinaryDataSet(interactionFile, bedFile, bimFile, famFile);
		reader = new BinaryDataSetReader(dataSet);
	}
	
	/**
	 * Test method for {@link se.kirc.geisa.data.plink.binary.BinaryDataSetReader#loadIndividuals()}.
	 * @throws IOException 
	 */
	@Test
	public void testLoadIndividuals() throws IOException {
		Collection<IndividualEntry> entries = reader.loadIndividuals();
		int index = 0;
		
		for (IndividualEntry entry : entries)
			assertEquals(entry.getId(), "ind" + index++);
//		reader.loadIndividuals();
//		reader.loadMarkers();
//		reader.loadGenotypes(store);
//		reader.loadInteractionData(store);
	}
	
	
}
