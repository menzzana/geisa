/*
 * HelpOption.java
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
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import se.kirc.geisa.Messages;

/**
 * This class handles the help option.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class HelpOption extends AbstractOptionHandler {
	private static final long serialVersionUID = 1977375719719456676L;
	
	/**
	 * All the available options.
	 */
	private Options options;

	/**
	 * Constructs a new option handler.
	 * 
	 * @param options
	 *            all the available options.
	 */
	public HelpOption(Options options) {
		this.options = options;

		// Extract the description message for the option.
		String message = Messages.getString("options.help");

		// Add the option.
		option = new Option("h", "help", false, message);
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
		// If the help option is specified, print the help and return false.
		if (cmd.hasOption(option.getOpt())) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("geisa", options);

			throw new OptionHandlerException();
		}
	}
}
