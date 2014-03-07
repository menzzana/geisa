/**
 * 
 */
package se.kirc.geisa.data.plink;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.kirc.geisa.data.plink.Genotype;


/**
 * @author danuve
 * 
 */
public class GenotypeTest {

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.Genotype#getValue()}.
	 */
	@Test
	public void testGetValue() {
		// Assert that the values for each genotype is consistent with that of
		// PLINK.
		assertEquals(Genotype.HOMOZYGOTE_PRIMARY.getValue(), 0);
		assertEquals(Genotype.UNKNOWN.getValue(), 1);
		assertEquals(Genotype.HETEROZYGOTE.getValue(), 2);
		assertEquals(Genotype.HOMOZYGOTE_SECONDARY.getValue(), 3);
	}

	/**
	 * Test method for
	 * {@link se.kirc.geisa.data.plink.Genotype#getByValue(java.lang.String)}.
	 */
	@Test
	public void testGetByValueString() {
		// Assert that the values for each genotype is consistent with that of
		// PLINK.
		assertEquals(Genotype.getByValue("-1"), Genotype.UNKNOWN);
		assertEquals(Genotype.getByValue("0"), Genotype.HOMOZYGOTE_PRIMARY);
		assertEquals(Genotype.getByValue("1"), Genotype.UNKNOWN);
		assertEquals(Genotype.getByValue("2"), Genotype.HETEROZYGOTE);
		assertEquals(Genotype.getByValue("3"), Genotype.HOMOZYGOTE_SECONDARY);
	}

	/**
	 * Test method for {@link se.kirc.geisa.data.plink.Genotype#getByValue(int)}
	 * .
	 */
	@Test
	public void testGetByValueInt() {
		// Assert that the values for each genotype is consistent with that of
		// PLINK.
		assertEquals(Genotype.getByValue(-1), Genotype.UNKNOWN);
		assertEquals(Genotype.getByValue(0), Genotype.HOMOZYGOTE_PRIMARY);
		assertEquals(Genotype.getByValue(1), Genotype.UNKNOWN);
		assertEquals(Genotype.getByValue(2), Genotype.HETEROZYGOTE);
		assertEquals(Genotype.getByValue(3), Genotype.HOMOZYGOTE_SECONDARY);
	}

}
