package br.com.adp.adpr.test;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.adp.adpr.test.bo.ClientBO;
import br.com.adp.adpr.test.dao.BaseDAO;
import br.com.adp.adpr.test.db.ConnectionException;
import br.com.adp.adpr.test.model.Client;
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

				showClients();

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

	protected static void showClients() throws SQLException {
		Map<String, Client> clientsList = new HashMap<String, Client>();

		try {
			final ClientBO clientBO = new ClientBO();
			clientsList = clientBO.getClientsList();
		} catch (final SQLException e) {
			logger.error(e.getMessage());
			throw e;
		}

		final Iterator<String> clientKeys = clientsList.keySet().iterator();

		int i = 1;
		while (clientKeys.hasNext()) {
			try {

				final String key = clientKeys.next();
				final Client client = clientsList.get(key);

				logger.info("Client " + client.getId() + " loaded" + " - " + i);
				i++;
			} catch (final Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		}

		logger.info("Total of " + clientsList.size() + " clients");
	}

}
