/*
 * CutoffOption.java
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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import se.kirc.geisa.Messages;
import se.kirc.geisa.conf.Configuration;
import se.kirc.geisa.conf.InvalidValueException;

/**
 * This class handles the cut off option.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class CutoffOption extends AbstractOptionHandler {
	private static final long serialVersionUID = 9213474593842220265L;
	
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
	public CutoffOption(Configuration configuration) {
		// Store a reference to the configuration.
		this.configuration = configuration;

		// Extract the description message for the option.
		String message = Messages.getString("options.help.cutoff");

		// Add the option.
		option = new Option("c", "cutoff", true, message);
		option.setArgName("n");
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
			// Get the argument.
			String value = cmd.getOptionValue(option.getOpt());
			int cutoff = Integer.parseInt(value);

			// Save the value to the configuration.
			try {
				configuration.setCutOff(cutoff);
			} catch (InvalidValueException e) {
				// Non-valid number.
				throw new OptionHandlerException(String.format(
						Messages.getString("options.error.cutoff"),
						e.getMessage()));
			}
		}
	}

}
