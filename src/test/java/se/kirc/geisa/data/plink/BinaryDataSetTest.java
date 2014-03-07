/**
 * 
 */
package se.kirc.geisa.data.plink;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import se.kirc.geisa.data.plink.binary.BinaryDataSet;


/**
 * @author danuve
 *
 */
public class BinaryDataSetTest {
	private File interactionFile;
	private File bedFile;
	private File bimFile;
	private File famFile;
	
	private BinaryDataSet dataSet;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		interactionFile = new File("./src/test/resources/test.txt");
		bedFile = new File("./src/test/resources/test.bed");
		bimFile = new File("./src/test/resources/test.bim");
		famFile = new File("./src/test/resources/test.fam");
		
		dataSet = new BinaryDataSet(interactionFile, bedFile, bimFile, famFile);
	}

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.binary.BinaryDataSet#getBEDFile()}.
	 */
	@Test
	public void testGetBEDFile() {
		assertEquals(bedFile, dataSet.getBEDFile());
	}

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.binary.BinaryDataSet#getBIMFile()}.
	 */
	@Test
	public void testGetBIMFile() {
		assertEquals(bimFile, dataSet.getBIMFile());
	}

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.binary.BinaryDataSet#getFAMFile()}.
	 */
	@Test
	public void testGetFAMFile() {
		assertEquals(famFile, dataSet.getFAMFile());
	}

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.AbstractDataSet#getInteractionFile()}.
	 */
	@Test
	public void testGetInteractionFile() {
		assertEquals(interactionFile, dataSet.getInteractionFile());
	}

}
