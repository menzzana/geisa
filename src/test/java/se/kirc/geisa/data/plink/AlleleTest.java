package se.kirc.geisa.data.plink;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.kirc.geisa.data.plink.Allele;


public class AlleleTest {

	@Test
	public void testGetValue() {
		// Assert that the values for each allele is consistent with that of
		// PLINK.
		assertEquals(Allele.UNKNOWN.getValue(), '0');
		assertEquals(Allele.A.getValue(), 'A');
		assertEquals(Allele.C.getValue(), 'C');
		assertEquals(Allele.T.getValue(), 'T');
		assertEquals(Allele.G.getValue(), 'G');
	}

	@Test
	public void testGetByValueString() {
		// Assert that the values for each allele is consistent with that of
		// PLINK.
		assertEquals(Allele.getByValue("0"), Allele.UNKNOWN);
		assertEquals(Allele.getByValue("9"), Allele.UNKNOWN);
		assertEquals(Allele.getByValue("A"), Allele.A);
		assertEquals(Allele.getByValue("C"), Allele.C);
		assertEquals(Allele.getByValue("T"), Allele.T);
		assertEquals(Allele.getByValue("G"), Allele.G);
	}

	@Test
	public void testGetByValueChar() {
		// Assert that the values for each allele is consistent with that of
		// PLINK.
		assertEquals(Allele.getByValue('0'), Allele.UNKNOWN);
		assertEquals(Allele.getByValue('9'), Allele.UNKNOWN);
		assertEquals(Allele.getByValue('A'), Allele.A);
		assertEquals(Allele.getByValue('C'), Allele.C);
		assertEquals(Allele.getByValue('T'), Allele.T);
		assertEquals(Allele.getByValue('G'), Allele.G);
	}

}
