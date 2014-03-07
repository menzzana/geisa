                                        /*
 * ResultColumn.java
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

/**
 * This enum represents the various columns used in the output.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public enum ResultColumn {
	PERM("perm"), INTERACTION("Interaction_marker"),
	CHR("Chr_test_marker"), SNP("Test_marker"), ORII("ORa_double_exposure"), ORIIL("ORa_double_exposure_lower_limit"), 
	ORIIH("ORa_double_exposure_higher_limit"), ORIO("ORa_test_marker"), ORIOL("ORa_test_marker_lower_limit"), 
	ORIOH("ORa_test_marker_higher_limit"), OROI("ORa_risk_factor"), 
	OROIL("ORa_risk_factor_lower_limit"), OROIH("ORa_risk_factor_higher_limit"), AP("AP"), APL("AP_L"), APH("AP_H"), 
	APP("AP_pvalue"), STABLELRA("Stable_additive_logistic_regression"), MULT("Multiplicative_interaction_term_pvalue"), ORMII("ORm_interaction"), ORMIIL("ORm_interaction_L"), 
	ORMIIH("ORm_interaction_H"), ORMIO("ORm_testmarker"), ORMIOL("ORm_testmarker_L"), ORMIOH("ORm_testmarker_H"), 
	ORMOI("ORm_riskfactor"), ORMOIL("ORm_riskfactor_L"), ORMOIH("ORm_riskfactor_H"), STABLELRM("Stable_multiplicative_logistic_regression"), IND00_0("No_controls_test_0_risk_0"), 
	IND00_1("No_cases_test_0_risk_0"), IND10_1("No_cases_test_1_risk_0"), IND10_0("No_controls_test_1_risk_0"), 
	IND01_1("No_cases_test_0_risk_1"), IND01_0("No_controls_test_0_risk_1"), IND11_1("No_cases_test_1_risk_1"), 
	IND11_0("No_controls_test_1_risk_1"), MINOR("Test_marker_minor_allele"), MAJOR("Test_marker_major_allele"), RISK("Test_marker_risk_allele"), 
	RECODE("recode_code"),THRESHOLD("Temporary_threshold");

	/**
	 * The key for the enum instance.
	 */
	private String key;

	/**
	 * Constructs a new ResultColumn with the given key.
	 * 
	 * @param key
	 *            the key of the enum.
	 */
	ResultColumn(String key) {
		this.key = key;
	}

	/**
	 * Returns the key of the enum.
	 * 
	 * @return the key.
	 */
	public String getKey() {
		return key;
	}
}
