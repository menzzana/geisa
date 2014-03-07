/*
 * AlleleSummary.java
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
package se.kirc.geisa;

import java.io.Serializable;

import se.kirc.geisa.data.plink.Allele;

/**
 * A class used for holding summary information of the various alleles used in
 * an analysis.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class AlleleSummary implements Serializable {
	private static final long serialVersionUID = -8028481032126666195L;

	/**
	 * The allele determined to be the risk allele.
	 */
	private Allele riskAllele;

	/**
	 * The major allele (most frequent allele).
	 */
	private Allele majorAllele;

	/**
	 * The minor allele (least frequent allele).
	 */
	private Allele minorAllele;

	/**
	 * The ratio of the major allele.
	 */
	private double majorRatio;

	/**
	 * The ratio of the minor allele.
	 */
	private double minorRatio;

	/**
	 * The ratio of the major allele among the controls.
	 */
	private double controlMajorRatio;

	/**
	 * The ratio of the minor allele among the controls.
	 */
	private double controlMinorRatio;

	/**
	 * The ratio of the major allele among the cases.
	 */
	private double caseMajorRatio;

	/**
	 * The ratio of the minor allele among the cases.
	 */
	private double caseMinorRatio;

	/**
	 * Returns the risk allele.
	 * 
	 * @return the risk allele.
	 */
	public Allele getRiskAllele() {
		return riskAllele;
	}

	/**
	 * Sets the risk allele.
	 * 
	 * @param riskAllele
	 *            the risk allele to set.
	 */
	public void setRiskAllele(Allele riskAllele) {
		this.riskAllele = riskAllele;
	}

	/**
	 * Returns the major allele.
	 * 
	 * @return the major allele.
	 */
	public Allele getMajorAllele() {
		return majorAllele;
	}

	/**
	 * Sets the major allele.
	 * 
	 * @param majorAllele
	 *            the major allele to set.
	 */
	public void setMajorAllele(Allele majorAllele) {
		this.majorAllele = majorAllele;
	}

	/**
	 * Returns the minor allele.
	 * 
	 * @return the minor allele.
	 */
	public Allele getMinorAllele() {
		return minorAllele;
	}

	/**
	 * Sets the minor allele.
	 * 
	 * @param minorAllele
	 *            the minor allele to set.
	 */
	public void setMinorAllele(Allele minorAllele) {
		this.minorAllele = minorAllele;
	}

	/**
	 * Returns the major ratio.
	 * 
	 * @return the major ratio.
	 */
	public double getMajorRatio() {
		return majorRatio;
	}

	/**
	 * Sets the major ratio.
	 * 
	 * @param majorRatio
	 *            the ratio to set.
	 */
	public void setMajorRatio(double majorRatio) {
		this.majorRatio = majorRatio;
	}

	/**
	 * Returns the minor ratio.
	 * 
	 * @return the minor ratio.
	 */
	public double getMinorRatio() {
		return minorRatio;
	}

	/**
	 * Sets the minor ratio.
	 * 
	 * @param minorRatio
	 *            the ratio to set.
	 */
	public void setMinorRatio(double minorRatio) {
		this.minorRatio = minorRatio;
	}

	/**
	 * Returns the major ratio among the controls.
	 * 
	 * @return the major ratio among the controls.
	 */
	public double getControlMajorRatio() {
		return controlMajorRatio;
	}

	/**
	 * Sets the major ratio among the controls.
	 * 
	 * @param controlMajorRatio
	 *            the ratio to set.
	 */
	public void setControlMajorRatio(double controlMajorRatio) {
		this.controlMajorRatio = controlMajorRatio;
	}

	/**
	 * Returns the minor ratio among the controls.
	 * 
	 * @return the minor ratio among the controls.
	 */
	public double getControlMinorRatio() {
		return controlMinorRatio;
	}

	/**
	 * Sets the minor ratio among the controls.
	 * 
	 * @param controlMinorRatio
	 *            the ratio to set.
	 */
	public void setControlMinorRatio(double controlMinorRatio) {
		this.controlMinorRatio = controlMinorRatio;
	}

	/**
	 * Returns the major ratio among the cases.
	 * 
	 * @return the major ratio among the cases.
	 */
	public double getCaseMajorRatio() {
		return caseMajorRatio;
	}

	/**
	 * Sets the major ratio among the cases.
	 * 
	 * @param caseMajorRatio
	 *            the ratio to set.
	 */
	public void setCaseMajorRatio(double caseMajorRatio) {
		this.caseMajorRatio = caseMajorRatio;
	}

	/**
	 * Returns the minor ratio among the cases.
	 * 
	 * @return the minor ratio among the cases.
	 */
	public double getCaseMinorRatio() {
		return caseMinorRatio;
	}

	/**
	 * Sets the minor ratio among the cases.
	 * 
	 * @param caseMinorRatio
	 *            the ratio to set.
	 */
	public void setCaseMinorRatio(double caseMinorRatio) {
		this.caseMinorRatio = caseMinorRatio;
	}
	
	public boolean isRiskAllele(Allele allele) {
		return riskAllele.equals(allele);
	}
}
