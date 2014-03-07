/*
 * OptionsParser.java
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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import se.kirc.geisa.conf.Configuration;

/**
 * This class parses all the options from the command line.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class OptionsParser {
	/**
	 * A reference to the configuration.
	 */
	private Configuration configuration;

	/**
	 * Remaining arguments.
	 */
	private String[] remains;

	/**
	 * Constructs a new options parser with an empty configuration and no
	 * options.
	 * 
	 * @param args
	 *            the command line arguments.
	 * @throws ParseException
	 *             if the parser fails to parse the options.
	 * @throws OptionHandlerException
	 *             if the configuration options cannot be set.
	 */
	public OptionsParser(String[] args) throws ParseException,
			OptionHandlerException {
		this(new Configuration(), args);
	}

	/**
	 * Constructs a new options parser with a specified base configuration and
	 * no options.
	 * 
	 * @param configuration
	 *            the configuration to modify.
	 * @param args
	 *            the command line arguments.
	 * @throws ParseException
	 *             if the parser fails to parse the options.
	 * @throws OptionHandlerException
	 *             if the configuration options cannot be set.
	 */
	public OptionsParser(Configuration configuration, String[] args)
			throws ParseException, OptionHandlerException {
		this(configuration, new Options(), args);
	}

	/**
	 * Constructs a new options parser with an empty configuration and the
	 * specified options.
	 * 
	 * @param options
	 *            the Options to which all other options will be added.
	 * @param args
	 *            the command line arguments.
	 * @throws ParseException
	 *             if the parser fails to parse the options.
	 * @throws OptionHandlerException
	 *             if the configuration options cannot be set.
	 */
	public OptionsParser(Options options, String[] args) throws ParseException,
			OptionHandlerException {
		this(new Configuration(), options, args);
	}

	/**
	 * Constructs a new options parser with a specified base configuration and
	 * the specified options.
	 * 
	 * @param configuration
	 *            the configuration to modify.
	 * @param options
	 *            the Options to which all other options will be added.
	 * @param args
	 *            the command line arguments.
	 * @throws ParseException
	 *             if the parser fails to parse the options.
	 * @throws OptionHandlerException
	 *             if the configuration options cannot be set.
	 */
	public OptionsParser(Configuration configuration, Options options,
			String[] args) throws ParseException, OptionHandlerException {
		this.configuration = configuration;

		// Parse the options.
		parseOptions(options, args);
	}

	/**
	 * Parse all the options with the given command line arguments.
	 * 
	 * @param options
	 *            all the options.
	 * @param args
	 *            the command line arguments.
	 * @throws ParseException
	 *             if the parser fails to parse the options.
	 * @throws OptionHandlerException
	 *             if the configuration ioptions cannot be set.
	 */
	private void parseOptions(Options options, String[] args)
			throws ParseException, OptionHandlerException {
		// Add default options.
		List<OptionHandler> handlers = buildOptions(options);

		// Create a command line parser and try to parse the commands.
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		// Handle all the options.
		for (OptionHandler handler : handlers)
			handler.handle(cmd);

		// Store a reference to the remaining arguments.
		remains = cmd.getArgs();
	}

	/**
	 * Build a list of all the option handlers based on the given options.
	 * 
	 * @param options
	 *            the Options to which all other options will be added.
	 * @return a list of all option handlers.
	 */
	private List<OptionHandler> buildOptions(Options options) {
		LinkedList<OptionHandler> handlers = new LinkedList<OptionHandler>();

		// Add default option handlers.
		// TODO: Make this automatic and let each Option register itself, or
		// dynamically load the options using a lookup facility.
		handlers.add(new BatchSizeOption(configuration));
		handlers.add(new BinaryDataSetOption(configuration));
		handlers.add(new DataStoreTypeOption(configuration));
		handlers.add(new HelpOption(options));
		handlers.add(new InteractionFileOption(configuration));
		handlers.add(new OutputDirectoryOption(configuration));
		handlers.add(new PermutationsOption(configuration));
		handlers.add(new QueueSizeOption(configuration));
		handlers.add(new SeedOption(configuration));
		handlers.add(new WorkersOption(configuration));
		handlers.add(new CutoffOption(configuration));
		handlers.add(new ModelOption(configuration));
		handlers.add(new MarkerFileOption(configuration));
		handlers.add(new IterationOption(configuration));
		handlers.add(new ThresholdOption(configuration));
		handlers.add(new LimitFileOption(configuration));
		handlers.add(new PermutationOutputOption(configuration));
		handlers.add(new NegativeAPPOption(configuration));
		
		// Add all handlers to the options.
		for (OptionHandler handler : handlers)
			options.addOption(handler.getOption());

		return handlers;
	}

	/**
	 * Returns all the remaining arguments (which were left unparsed).
	 * 
	 * @return all remaining arguments.
	 */
	public String[] getRemainingArguments() {
		return remains == null ? new String[] {} : remains;
	}

	/**
	 * Returns the configuration instance.
	 * 
	 * @return the configuration instance.
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

}
