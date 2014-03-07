package se.kirc.geisa.data.store;

import java.io.Serializable;

import se.kirc.geisa.data.plink.Allele;

/**
 * This class represents a marker entry as used in the PLINK binary format in
 * SNP major mode. It consists of all genotypes for the specific marker, binary
 * encoded as two bits. Please see
 * http://pngu.mgh.harvard.edu/~purcell/plink/binary.shtml for more information.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public class MarkerEntry implements Serializable {
	private static final long serialVersionUID = 8991549872447602107L;
	private String chromosome;
	private String id;
	private double geneticDistance;
	private int basePairPosition;
	private Allele firstAllele;
	private Allele secondAllele;

	/**
	 * Constructs a new MarkerEntry.
	 * 
	 * @param id
	 *            the ID of the marker
	 */
	public MarkerEntry(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public Allele getFirstAllele() {
		return firstAllele;
	}

	public void setFirstAllele(Allele firstAllele) {
		this.firstAllele = firstAllele;
	}

	public Allele getSecondAllele() {
		return secondAllele;
	}

	public void setSecondAllele(Allele secondAllele) {
		this.secondAllele = secondAllele;
	}

	public double getGeneticDistance() {
		return geneticDistance;
	}

	public void setGeneticDistance(double geneticDistance) {
		this.geneticDistance = geneticDistance;
	}

	public int getBasePairPosition() {
		return basePairPosition;
	}

	public void setBasePairPosition(int basePairPosition) {
		this.basePairPosition = basePairPosition;
	}
}
