/*
 * ConnectionHelper.java
 *
 *
 * Copyright (c) 2009 Automatic Data Processing, Inc.
 * 1 ADP Boulevard, Roseland, New Jersey, 07068, U.S.A.
 * All rights reserved.
 */
package br.com.adp.adpr.test.util;

import java.io.IOException;
import java.sql.*;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.adp.adpr.test.exception.ConnectionError;

public final class MySqlConnectionHelper implements ConnectionHelperInterface {

	private static ThreadLocal<Connection> connection = new ThreadLocal<Connection>();

	//private static Connection connection;

	private static final String JDBC_DRIVER_STRING = "com.mysql.jdbc.Driver";

	/**
	 * As a singleton, the constructor is private
	 */
	public MySqlConnectionHelper() {
		/* FIXME (pesa) Transform this into ConnectionFactory
		 * Also, get the connection from a connection pool
		 */
	}

	/**
	 * This method create an oracle database connection.
	 *
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws ConnectionError 
	 */
	public void initializeConnectionHelper() throws SQLException, ClassNotFoundException, ConnectionError {
		final Logger LOG = AppLogger.getLogger();

		LOG.info("Initializing DB connection");

		/* Add try catch */
		PropertiesHelper propHelper;
		try {
			propHelper = new PropertiesHelper();

		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
			throw new ConnectionError("Error reading properties file (IOException)");
		} catch (final NamingException e) {
			LOG.error(e.getMessage(), e);
			throw new ConnectionError("Error reading properties file (NamingException)");
		}

		String host = null;
		String port = null;
		String sid = null;
		String user = null;
		String password = null;
		String jdbcURL = null;

		user = propHelper.getProperty("Username");
		password = propHelper.getProperty("Password");
		sid = propHelper.getProperty("DatabaseName");
		port = propHelper.getProperty("ServerPort");
		host = propHelper.getProperty("ServerName");

		jdbcURL = "jdbc:mysql://" + host + ":" + port + "/" + sid;

		try {
			LOG.info("Connecting to Catalog database. URL: " + jdbcURL);
			//DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Class.forName(JDBC_DRIVER_STRING);
			final long start = TimeKeeper.sysDate();
			connection.set(DriverManager.getConnection(jdbcURL, user, password));
			connection.get().setAutoCommit(false);
			LOG.info("Connected to Catalog database!");
			LOG.info(TimeKeeper.getElapsedTime(start, "Database Connection"));
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ConnectionError("Error connecting to the catalog", e);
		}

	}

	//	public static void initializeConnectionHelper(final Connection connection) {
	//		LOG.debug("Setting database connection using by-pass");
	//		ConnectionHelper.connection = connection.get;
	//	}

	/**
	 * Getter
	 *
	 * @return
	 * @throws ConnectionError
	 * @throws SQLException
	 * @throws DatabaseException
	 */
	public Connection getConnection() throws ConnectionError {
		final Logger LOG = AppLogger.getLogger();

		LOG.info("Getting connection to the database");
		if (null == connection.get()) {
			LOG.info("There is no existing connection!");
			try {
				initializeConnectionHelper();
			} catch (final ClassNotFoundException e) {
				LOG.error(e);
				throw new ConnectionError(e.getMessage(), e);
			} catch (final SQLException e) {
				LOG.error(e);
				throw new ConnectionError(e.getMessage(), e);
			}
		} else {
			LOG.info("Reusing existing connection!");
		}
		return connection.get();
	}

	public void closeConnection() {

		final Logger LOG = AppLogger.getLogger();

		LOG.debug("Closing database connection in this THREAD!");
		if (MySqlConnectionHelper.connection != null) {
			try {
				MySqlConnectionHelper.connection.get().close();
				connection.remove();
				LOG.debug("THREAD Database connection closed and removed!");
			} catch (final SQLException e) {
				LOG.debug("THREAD Error during db close connection!");
			}
		}

	}

}
