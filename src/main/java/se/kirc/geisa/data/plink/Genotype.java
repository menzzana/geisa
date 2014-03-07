package se.kirc.geisa.data.plink;

import java.util.EnumSet;

public enum Genotype {
	HOMOZYGOTE_PRIMARY(0), UNKNOWN(1), HETEROZYGOTE(2), HOMOZYGOTE_SECONDARY(3);

	private int value;

	Genotype(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Convert a String value to a Genotype. If the String value is not known,
	 * the return value will be UNKNOWN.
	 * 
	 * @param value
	 *            the String value to convert
	 * @return the corresponding Genotype
	 */
	public static Genotype getByValue(String value) {
		return getByValue(Integer.parseInt(value));
	}

	public static Genotype getByValue(int value) {
		// The default genotype is unknown.
		Genotype result = Genotype.UNKNOWN;

		// Iterate through all known genotypes.
		for (final Genotype genotype : EnumSet.allOf(Genotype.class)) {
			// If the genotype matches, save the result.
			if (genotype.getValue() == value) {
				result = genotype;
			}
		}

		// Return the correct genotype.
		return result;
	}
}
