/**
 * 
 */
package br.com.adp.adpr.test.util;

import org.apache.log4j.Logger;

/**
 * @author $Author: fernando $
 * @version $Id: TimeKeeper.java,v 1.3 2012/04/27 18:47:50 fernando Exp $
 */
public class TimeKeeper {

	public static long sysDate() {
		return System.nanoTime();
	}

	public static void logElapsedTime(final Logger LOG, final long start) {
		/*
		 * FIXME this method should not be used All references must be replaced by getElapsedTime
		 */
		final long end = System.nanoTime();
		final double elapsedTime = (end - start) / 1000000000.0;
		LOG.debug(" elapsed time: '" + elapsedTime + "'");
	}

	public static String getElapsedTime(final long start) {
		final long end = System.nanoTime();
		final double elapsedTime = (end - start) / 1000000000.0;
		return new String(" elapsed time: '" + elapsedTime + "'");
	}

	public static String getElapsedTime(final long start, final String prefix) {
		return prefix.concat(TimeKeeper.getElapsedTime(start));
	}
}
