/*
 * InteractionDecoder.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import se.kirc.geisa.Messages;

/**
 * A decoder for parsing a stream from an interaction variable file.
 * The header line is checked for errors, and multiple covariate columns can be entered
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public class InteractionDecoder {
	private BufferedReader reader;
	private int individual_column;
	private int env_column;

	/**
	 * Constructs a new {@link InteractionDecoder} from a given
	 * {@link InputStream}.
	 * 
	 * @param inputStream
	 *            the input stream.
	 * @throws IOException
	 *             if a read error occurs.
	 */
	public InteractionDecoder(InputStream inputStream) throws IOException {
		this(new InputStreamReader(inputStream));
	}

	/**
	 * Constructs a new {@link InteractionDecoder} from a given {@link Reader}.
	 * 
	 * @param reader
	 *            the reader.
	 * @throws IOException
	 *             if a read error occurs.
	 */
	
	public boolean isENVColumnPresent() {
		return env_column>=0;
	}
	
	public InteractionDecoder(Reader reader) throws IOException {
		this(new BufferedReader(reader));
	}

	/**
	 * Constructs a new {@link InteractionDecoder} from a given
	 * {@link BufferedReader}.
	 * 
	 * @param reader
	 *            the reader.
	 * @throws IOException
	 *             if a read error.
	 */
	public InteractionDecoder(BufferedReader reader) throws IOException {
		StringTokenizer tokenizer;
		int i1;
		String s1;
		
		// Store a reference to the reader.
		this.reader = reader;
		individual_column=-1;
		env_column=-1;

		// Discard the header line.
		tokenizer=new StringTokenizer(reader.readLine());
		// Check headers in file
		for (i1=0; tokenizer.hasMoreTokens(); i1++) {
			s1=tokenizer.nextToken();
			if (s1.equalsIgnoreCase("INDID"))
				individual_column=i1;
			if (s1.equalsIgnoreCase("ENV"))
				env_column=i1;
		}
		if (individual_column<0)
			throw new IOException(String.format(Messages.getString("options.error.corruptinteractionfile"),"individual"));
	}

	/**
	 * Read one entry from the interaction variable source.
	 * 
	 * @return an {@link InteractionEntry} if possible, otherwise null if no
	 *         more entries exist.
	 * @throws IOException
	 */
	public InteractionEntry readEntry() throws IOException {
		String line;
		List<Float> covariate=new ArrayList<Float>();
		String individualId=null;
		int variable=0;
		StringTokenizer tokenizer;
		String s1;
		
		if ((line = reader.readLine()) == null)
			return null;
		else {
			variable=-1;
			tokenizer = new StringTokenizer(line);
			for (int i1=0; tokenizer.hasMoreTokens(); i1++) {
				s1=tokenizer.nextToken();
				if (i1==individual_column) {
					individualId=s1;
					continue;
					}
				if (i1==env_column) {
					variable=Integer.parseInt(s1.replaceAll("NA", "-1"));
					variable=(int)Math.signum(variable);
					continue;
					}
				covariate.add(Float.parseFloat(s1.replaceAll("NA", "0")));
				}
			return new InteractionEntry(individualId, covariate, variable);
		}
	}
}