/*
 * InvalidValueException.java
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

/**
 * This exception is thrown if a configuration value is invalid, e.g. not within
 * bounds.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class OptionHandlerException extends Exception {
	private static final long serialVersionUID = -7687038303707736996L;

	/**
	 * Constructs a new exception.
	 * 
	 * @param message
	 */
	public OptionHandlerException() {
		super();
	}
	
	/**
	 * Constructs a new exception with the specified message.
	 * 
	 * @param message the message.
	 */
	public OptionHandlerException(String msg) {
		super(msg);
	}
}
