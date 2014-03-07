/*
 * BimDecoder.java
 * Copyright (C) 2011-2012  KIRC
 * 
 * This file is part of JEIRA.
 * 
 * JEIRA is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * JEIRA is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package se.kirc.geisa.data.plink.binary.bim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

import se.kirc.geisa.data.plink.Allele;

/**
 * This class reads data in PLINK BIM file/stream.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public class BimDecoder {
	private BufferedReader reader;

	/**
	 * This constructs a new BIM decoder based on the specified input stream.
	 * 
	 * @param inputStream
	 *            the input stream to read from
	 */
	public BimDecoder(InputStream inputStream) {
		this(new InputStreamReader(inputStream));
	}

	/**
	 * This constructs a new BIM decoder based on the specified reader.
	 * 
	 * @param reader
	 *            the reader to read from
	 */
	public BimDecoder(Reader reader) {
		this(new BufferedReader(reader));
	}

	/**
	 * This constructs a new BIM decoder based on the specified buffered reader.
	 * 
	 * @param reader
	 *            the buffered reader to read from
	 */
	public BimDecoder(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * Returns a BIM entry constructed from the source.
	 * 
	 * @return a BIM entry constructed from the source
	 * @throws IOException
	 *             if the reader fails to read a line from the source
	 */
	public BimEntry readEntry() throws IOException {
		String line;

		// If there's nothing to read, return null.
		if ((line = reader.readLine()) == null)
			return null;
		else {
			// Parse the line. Perhaps this should be checked? It's very naïve
			// to blindly read values like this.
			StringTokenizer tokenizer = new StringTokenizer(line);
			String chromosome = tokenizer.nextToken().trim();
			String id = tokenizer.nextToken();
			double geneticDistance = Double.parseDouble(tokenizer.nextToken());
			long basePosition = Long.parseLong(tokenizer.nextToken());
			Allele firstAllele = Allele.getByValue(tokenizer.nextToken());
			Allele secondAllele = Allele.getByValue(tokenizer.nextToken());

			// Construct and return a new BIM entry.
			return new BimEntry(chromosome, id, geneticDistance, basePosition,
					firstAllele, secondAllele);
		}
	}
}
