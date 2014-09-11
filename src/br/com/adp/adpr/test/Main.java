package br.com.adp.adpr.test;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.adp.adpr.test.dao.BaseDAO;
import br.com.adp.adpr.test.dao.UserDAO;
import br.com.adp.adpr.test.db.ConnectionException;
import br.com.adp.adpr.test.model.User;
import br.com.adp.adpr.test.util.AppLogger;

public class Main {

	public static final String VERSION = "ADPR Test version 2014-09-05 16:36:26";

	protected static final Logger logger = AppLogger.getLogger();

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws ConnectionException
	 */
	public static void main(final String[] args) {

		logger.info(VERSION);

		try {

			if (args.length > 0) {

				logger.info("Starting main program for client " + args[0]);

				final BaseDAO baseDao = new BaseDAO();
				final String version = baseDao.retrieveAppVersionFromDB();

				logger.info("Version from database: " + version);

				showUsers();

			} else {
				logger.info("Wrong parameters");
			}

		} catch (final InvalidParameterException e) {
			logger.error("Invalid configuration properties", e);
		} catch (final SQLException e) {
			logger.error("SQLException", e);
		} catch (final Exception e) {
			logger.error("Unhandled exception", e);
		}
	}

	protected static void showUsers() throws SQLException {
		Map<Integer, User> usersList = new LinkedHashMap<Integer, User>();

		try {
			final UserDAO userDAO = new UserDAO();
			usersList = userDAO.getUsersList();
		} catch (final SQLException e) {
			logger.error(e.getMessage());
			throw e;
		}

		final Iterator<Integer> userKeys = usersList.keySet().iterator();

		while (userKeys.hasNext()) {
			try {

				final Integer key = userKeys.next();
				final User user = usersList.get(key);

				logger.info("User " + user.getId() + " - " + user.getName() + " loaded");
			} catch (final Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		}

		logger.info("Total of " + usersList.size() + " users");
	}

}
