/*
 * AbstractResultConsumer.java
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class that implements the basics needed for consuming results. This
 * provides a convenient base class from which other consumer classes can easily
 * be derived.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public abstract class AbstractResultConsumer implements Runnable {
	/**
	 * A queue containing all future results to process.
	 */
	protected BlockingQueue<Future<Iterable<Map<ResultColumn, String>>>> queue;

	/**
	 * Semaphore used to determine when all results are submitted.
	 */
	protected AtomicBoolean done;

	/**
	 * A list of columns used for the output header.
	 */
	protected List<ResultColumn> columns;

	/**
	 * Sole constructor.
	 * 
	 * @param done
	 *            An atomic boolean used to signal that no more entries will be
	 *            put into the queue.
	 * @param columns
	 *            A list of columns to use as the output header.
	 * @param queue
	 *            The queue containing all future results.
	 */
	protected AbstractResultConsumer(AtomicBoolean done,
			List<ResultColumn> columns,
			BlockingQueue<Future<Iterable<Map<ResultColumn, String>>>> queue) {
		// Store a reference to the arguments.
		this.done = done;
		this.columns = columns;
		this.queue = queue;

		}
}
