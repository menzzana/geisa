/*
 * DimensionMismatchException.java
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
package se.kirc.math.regression;

/**
 * This exception is used when the expected dimensions are wrong for a
 * calculation, e.g. in a matrix multiplication of two matrices A (m x n) and B
 * (p x q) where n != p.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class DimensionMismatchException extends RuntimeException {
	private static final long serialVersionUID = -291122605903191468L;

	/**
	 * Constructs a default DimensionMismatchException.
	 */
	public DimensionMismatchException() {
		super();
	}

	/**
	 * Constructs a new DimensionMismatchException with the given message and
	 * cause.
	 * 
	 * @param message
	 *            the message.
	 * @param cause
	 *            the cause.
	 */
	public DimensionMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new DimensionMismatchException with the given message.
	 * 
	 * @param message
	 *            the message.
	 */
	public DimensionMismatchException(String message) {
		super(message);
	}

	/**
	 * Constructs a new DimensionMismatchException with the given cause.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public DimensionMismatchException(Throwable cause) {
		super(cause);
	}
}
