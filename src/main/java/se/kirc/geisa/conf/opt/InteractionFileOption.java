/*
 * InteractionFileOption.java
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
 * This class handles the interaction file option.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class InteractionFileOption extends AbstractOptionHandler {
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
	public InteractionFileOption(Configuration configuration) {
		// Store a reference to the configuration.
		this.configuration = configuration;

		// Extract the description message for the option.
		String message = Messages.getString("options.help.interactionfile");

		// Add the option.
		option = new Option("i", "ifile", true, message);
		option.setArgName("file");
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
			// Get the value.
			String value = cmd.getOptionValue(option.getOpt());

			// Save the value to the configuration.
			try {
				configuration.setInteractionFile(new File(value));
			} catch (InvalidValueException e) {
				// Non-valid values.
				throw new OptionHandlerException(String.format(
						Messages.getString("options.error.interactionfile"),
						e.getMessage()));
			}
		}
	}

}
