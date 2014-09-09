package br.com.adp.adpr.test.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class AppLogger {

	private static final Logger LOG_DEFAULT = Logger.getLogger(AppLogger.class);

	private static ThreadLocal<Logger> ME_LOG = new ThreadLocal<Logger>();

	/**
	 * Return Logger
	 * @param logName
	 * @return
	 */
	public static Logger getLogger() {
		return LOG_DEFAULT;
	}

	public static void remove() {
		ME_LOG.remove();
	}

	/**
	 * @param logName
	 * @return
	 */
	public static Logger addLogger(final String logName) {
		String logPath = null;
		final String logLayout = null;

		try {

			logPath = getLogPath(logName);
			final RollingFileAppender fileAppender = new RollingFileAppender();
			fileAppender.setLayout(new PatternLayout(logLayout));
			fileAppender.setFile(logPath);
			fileAppender.setMaxFileSize("100000KB");
			//fileAppender.setMaxBackupIndex(10);
			fileAppender.setName(logName);
			fileAppender.activateOptions();
			ME_LOG.set(Logger.getLogger(logName));
			ME_LOG.get().setAdditivity(false);
			ME_LOG.get().addAppender(fileAppender);

			ME_LOG.get().info("LOG CREATED: " + logPath);
			LOG_DEFAULT.info("Migration Engine LOG CREATED: " + logPath);

		} catch (final Exception e) {
			LOG_DEFAULT.error("ERROR CREATING NEW Migration Engine LOG: " + logPath);
		}

		return ME_LOG.get();
	}

	/**
	 * Return Log Path
	 * @param logName
	 * @return
	 */
	public static String getLogPath(final String logName) {
		return "logs/" + logName;
	}

}
