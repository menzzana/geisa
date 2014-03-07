/*
 * Main.java
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

import java.io.IOException;

import org.apache.commons.cli.ParseException;

import se.kirc.geisa.Messages;
import se.kirc.geisa.conf.Configuration;
import se.kirc.geisa.conf.ConfigurationValidator;
import se.kirc.geisa.conf.DefaultConfiguration;
import se.kirc.geisa.conf.InvalidValueException;
import se.kirc.geisa.conf.opt.OptionHandlerException;
import se.kirc.geisa.conf.opt.OptionsParser;

/**
 * This is the main class of the application containing its entry point. It
 * takes care of parsing and validating the command line arguments then passing
 * on control to the computational part of the program.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public class Main {
	/**
	 * Main entry point of the program.
	 * 
	 * @param args
	 *            The command line arguments.
	 * @throws ParseException
	 * @throws OptionHandlerException
	 * @throws InvalidValueException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ParseException,
			OptionHandlerException, IOException, InterruptedException {
		Configuration configuration = null;
		
		// This throws exceptions if the configuration is not complete.
		try {
			// In case no parameters have been defined
			if (args.length==0) {
				System.err.println(Messages.getString("configuration.error.parameters.missing"));
				System.exit(1);
				}
			// Parse the program arguments and get the resulting configuration.
			// Use the DefaultConfiguration as the base.
			OptionsParser parser = new OptionsParser(
					new DefaultConfiguration(), args);
			configuration = parser.getConfiguration();

			// Validate the configuration.
			ConfigurationValidator.validate(configuration);
		} catch (OptionHandlerException e) {
			String message = e.getMessage();
			
			if (message != null)
				System.err.println(message);
			
			System.exit(1);
		} catch (InvalidValueException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// Hand over program control to the Geisa component.
		Geisa geisa = new Geisa(configuration);
		geisa.run();
	}
}
