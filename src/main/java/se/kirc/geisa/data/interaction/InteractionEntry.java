/*
 * AbstractOptionHandler.java
 * Copyright (C) 2011-2012  KIRC
 * 
 * This file is part of GEISA. GEISA is an upgrade of Jeira by Daniel Uvehag
 * 
 * GEISA is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * GEISA is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.kirc.geisa.data.interaction;

import java.io.Serializable;
import java.util.List;

/**
 * A class used for holding information about an entry in an interaction
 * variable file source.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class InteractionEntry implements Serializable {
	private static final long serialVersionUID = 1287633763107199399L;

	private String individualId;
	private List<Float> covariance;
	private int variable;

	/**
	 * Constructs a new {@link InteractionEntry} with the specified parameters.
	 * 
	 * @param individualId
	 *            the individual's ID.
	 * @param covariate
	 *            the covariance.
	 * @param variable
	 *            the interaction variable.
	 */
	public InteractionEntry(String individualId, List<Float> covariate, int variable) {
		// Store the values.
		this.individualId = individualId;
		this.covariance = covariate;
		this.variable = variable;
	}

	/**
	 * Returns the individual's ID.
	 * 
	 * @return the individual's ID.
	 */
	public String getIndividualId() {
		return individualId;
	}

	/**
	 * Sets the individual's ID.
	 * 
	 * @param individualId
	 *            the individual's ID.
	 */
	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	/**
	 * Returns the covariance.
	 * 
	 * @return the covariance.
	 */
	public List<Float> getCovariance() {
		return covariance;
	}

	/**
	 * Sets the covariance.
	 * 
	 * @param covariance
	 *            the covariance.
	 */
	public void setCovariance(List<Float> covariance) {
		this.covariance = covariance;
	}

	/**
	 * Returns the interaction variable.
	 * 
	 * @return the interaction variable.
	 */
	public int getInteractionVariable() {
		return variable;
	}

	/**
	 * Sets the interaction variable.
	 * 
	 * @param variable
	 *            the interaction variable.
	 */
	public void setInteractionVariable(int variable) {
		this.variable = variable;
	}
}
