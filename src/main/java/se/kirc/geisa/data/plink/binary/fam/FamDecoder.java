/*
 * BimEntry.java
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

package se.kirc.geisa.data.plink.binary.fam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

import se.kirc.geisa.data.plink.Sex;

/**
 * This class reads data in PLINK FAM file/stream.
 * 
 * @author Daniel Uvehag <daniel@uvehag.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 */
public class FamDecoder {
	private BufferedReader reader;

	/**
	 * This constructs a new FAM decoder based on the specified input stream.
	 * 
	 * @param inputStream
	 *            the input stream to read from
	 */
	public FamDecoder(InputStream inputStream) {
		this(new InputStreamReader(inputStream));
	}

	/**
	 * This constructs a new FAM decoder based on the specified reader.
	 * 
	 * @param reader
	 *            the reader to read from
	 */
	public FamDecoder(Reader reader) {
		this(new BufferedReader(reader));
	}

	/**
	 * This constructs a new FAM decoder based on the specified buffered reader.
	 * 
	 * @param reader
	 *            the buffered reader to read from
	 */
	public FamDecoder(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * Returns a FAM entry constructed from the source.
	 * 
	 * @return a FAM entry constructed from the source
	 * @throws IOException
	 *             if the reader fails to read a line from the source
	 */
	public FamEntry readEntry() throws IOException {
		String line;

		// If there's nothing to read, return null.
		if ((line = reader.readLine()) == null)
			return null;
		else {
			// Parse the line. Perhaps this should be checked? It's very naïve
			// to blindly read values like this.
			StringTokenizer tokenizer = new StringTokenizer(line);
			String familyId = tokenizer.nextToken();
			String individualId = tokenizer.nextToken();
			String paternalId = tokenizer.nextToken();
			String maternalId = tokenizer.nextToken();
			Sex sex = Sex.getByValue(tokenizer.nextToken());
			int phenotype = Integer.parseInt(tokenizer.nextToken());

			return new FamEntry(familyId, individualId, paternalId, maternalId,
					sex, phenotype);
		}
	}
}
