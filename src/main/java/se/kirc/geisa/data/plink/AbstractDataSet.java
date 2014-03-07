package se.kirc.geisa.data.plink;

import java.io.File;

public abstract class AbstractDataSet implements DataSet {
	protected File interaction;

	/**
	 * Constructs a new AbstractDataSet with interaction variable data.
	 * 
	 * @param interaction
	 *            the interaction variable reader
	 */
	public AbstractDataSet(File interaction) {
		this.interaction = interaction;
	}

	/**
	 * Returns the reader for the interaction variable stream.
	 * 
	 * @return the reader for the interaction variable stream
	 */
	public File getInteractionFile() {
		return interaction;
	}
}
