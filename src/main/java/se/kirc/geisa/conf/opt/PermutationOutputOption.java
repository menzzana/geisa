/*
 * MarkerFileOption.java
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
package se.kirc.geisa.conf.opt;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import se.kirc.geisa.Messages;
import se.kirc.geisa.conf.Configuration;
import se.kirc.geisa.conf.InvalidValueException;

/**
 * The Permutation Output option class in the application (driver).
 * 
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class PermutationOutputOption extends AbstractOptionHandler {
	private static final long serialVersionUID = -1826639615702189053L;

	/**
	 * A reference to the configuration.
	 */
	private Configuration configuration;
	
	/**
	 * Constructs a new option handler.
	 * 
	 * @param configuration
	 *            the configuration to modify.
	 */
	public PermutationOutputOption(Configuration configuration) {
		// Store a reference to the configuration.
		this.configuration = configuration;

		// Extract the description message for the option.
		String message = Messages.getString("options.help.permutationoutput");

		// Add the option.
		option = new Option("po", "permutationoutput", true, message);
		option.setArgName("R=Raw permutation output, T=Total permutation output");
	}
	
	/**
	 * The method which handles the option based on a given command line.
	 * 
	 * @param cmd
	 *            the command line to parse.
	 * @throws OptionHandlerException
	 *             if an error occurs while parsing the option or setting the
	 *             value.
	 */
	public void handle(CommandLine cmd) throws OptionHandlerException {
		if (cmd.hasOption(option.getOpt())) {
			configuration.setPermutationOutput(cmd.getOptionValue(option.getOpt()).toLowerCase().contains("r"));
			configuration.setTotalPermutationOutput(cmd.getOptionValue(option.getOpt()).toLowerCase().contains("t"));
		}
	}
}
