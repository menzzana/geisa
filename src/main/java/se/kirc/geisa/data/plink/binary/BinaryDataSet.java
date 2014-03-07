package se.kirc.geisa.data.plink.binary;

import java.io.File;

import se.kirc.geisa.data.plink.AbstractDataSet;

public class BinaryDataSet extends AbstractDataSet {
	private File bed;
	private File bim;
	private File fam;

	public BinaryDataSet(File interaction, File bed, File bim, File fam) {
		// Pass the store and interaction variable reader to the parent.
		super(interaction);

		this.bed = bed;
		this.bim = bim;
		this.fam = fam;
	}

	public File getBEDFile() {
		return bed;
	}

	public File getBIMFile() {
		return bim;
	}

	public File getFAMFile() {
		return fam;
	}
}
