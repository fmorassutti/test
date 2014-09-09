package br.com.adp.adpr.test.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.adp.adpr.test.util.AppLogger;
import br.com.adp.adpr.test.util.PropertiesHelper;


public class ConnectionManager {

	private static String DATABASE_PROPERTIES_FILE = "conf/database.properties";

	private static Connection mainConnection = null;

	private static HashMap<String, Connection> catalogConnections = null;

	/**
	 * store true is schema
	 */
	private static HashMap<Connection, Boolean> schemaVersion = new HashMap<Connection, Boolean>();

	/**
	 * Initialize the connection object using the data from database properties file (Reporting
	 * database)
	 */
	public Connection getCatalogConnection() throws ConnectionException {

		final Logger logger = AppLogger.getLogger();
		Connection connection = null;
		PropertiesHelper prop = null;

		if (mainConnection != null) {
			logger.info("returning existing catalog database connection");
			return mainConnection;
		}

		logger.info("Initializing Catalog database connection");

		try {
			prop = new PropertiesHelper();
		} catch (final IOException e) {
			logger.error(e.getMessage());
		} catch (final NamingException e) {
			logger.error(e.getMessage());
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		String host = null;
		String port = null;
		String sid = null;
		String user = null;
		String password = null;
		String url = null;
		String racUrl = null;
		String racEnv = null;

		try {
			host = prop.getProperty("ServerName");
			port = prop.getProperty("ServerPort");
			sid = prop.getProperty("DatabaseName");
			user = prop.getProperty("Username");
			password = prop.getProperty("Password");
			racEnv = prop.getProperty("RacEnvironment");
			racUrl = prop.getProperty("RacConnectionUrl");

		} catch (final Exception e) {
			logger.error(e);
			throw new ConnectionException("database.properties read error");
		}

		if (racEnv != null && racEnv.trim().toUpperCase().equals("TRUE") && racUrl != null) {
			url = "jdbc:oracle:thin:@" + racUrl;
		} else {
			url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
		}

		try {
			logger.info("Connecting to Reporting Catalog database");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			logger.info("Connected to Reporting Catalog database");

		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
			throw new ConnectionException(e);
		}

		mainConnection = connection;
		this.populateSchemaVersion(mainConnection);

		return connection;
	}

	public HashMap<String, Connection> getAvailableCatalogConnections() throws ConnectionException {
		if (catalogConnections == null) {
			catalogConnections = new PropertyConfigurator().configureConnections(ConnectionManager.DATABASE_PROPERTIES_FILE);
		} else {
			AppLogger.getLogger().info("returning existing catalog database connection");
		}

		return catalogConnections;
	}

	public static void setDatabasePropertiesFile(final String sFilePath) {
		ConnectionManager.DATABASE_PROPERTIES_FILE = sFilePath;
	}

	private void populateSchemaVersion(final Connection connection) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final String sql = "select count(1) is_94 from admdbversion where dbv_phpversion like '09.04%'";

		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				schemaVersion.put(connection, rs.getInt("is_94") > 0);
			}
		} catch (final SQLException e) {
			AppLogger.getLogger().error("Error on get version of connection from admdbversion", e);
		} finally {
			try {
				ps.close();
			} catch (final SQLException e) {
			}
			try {
				rs.close();
			} catch (final SQLException e) {
			}
		}
	}

	private class PropertyConfigurator {

		public HashMap<String, Connection> configureConnections(final String filePath) throws ConnectionException {

			final Logger logger = AppLogger.getLogger();
			final Properties properties = new Properties();
			final HashMap<String, Connection> connections = new HashMap<String, Connection>();

			try {
				properties.load(new FileInputStream(filePath));
			} catch (final FileNotFoundException e) {
				throw new ConnectionException("database.properties read error");
			} catch (final IOException e) {
				e.printStackTrace();
				throw new ConnectionException("database.properties read error");
			}

			final Enumeration<Object> em = properties.keys();

			String str;
			String tmp;
			String alias;
			String host = null;
			String port = null;
			String sid = null;
			String user = null;
			String password = null;
			String racUrl = null;
			String racEnv = null;
			String url = null;
			Connection connection;

			while (em.hasMoreElements()) {
				str = (String) em.nextElement();
				if (str.length() > 12 && str.substring(0, 12).equals("sdg.catalog.")) {
					tmp = str.substring(12);
					alias = tmp.substring(0, tmp.indexOf("."));
					if (connections.containsKey(alias)) {
						continue;
					}
					user = properties.getProperty("sdg.catalog." + alias + ".Username");
					password = properties.getProperty("sdg.catalog." + alias + ".Password");
					sid = properties.getProperty("sdg.catalog." + alias + ".DatabaseName");
					port = properties.getProperty("sdg.catalog." + alias + ".ServerPort");
					host = properties.getProperty("sdg.catalog." + alias + ".ServerName");
					racEnv = properties.getProperty("sdg.catalog." + alias + ".RacEnvironment");
					racUrl = properties.getProperty("sdg.catalog." + alias + ".RacConnectionUrl");
					connection = null;
					logger.info("Initializing Catalog database connection using alias '" + alias + "'");
					if (racEnv != null && racEnv.trim().toUpperCase().equals("TRUE") && racUrl != null) {
						url = "jdbc:oracle:thin:@" + racUrl;
					} else {
						url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
					}
					try {
						logger.info("Connecting to Reporting Catalog database using alias '" + alias + "'");
						DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
						Class.forName("oracle.jdbc.driver.OracleDriver");
						connection = DriverManager.getConnection(url, user, password);
						connection.setAutoCommit(false);
						logger.info("Connected to Reporting Catalog database using alias '" + alias + "'");

					} catch (final Exception e) {
						logger.error(e.getMessage(), e);
						throw new ConnectionException(e);
					}
					connections.put(alias, connection);
					ConnectionManager.this.populateSchemaVersion(connection);
				}
			}
			return connections;
		}
	}

	public static boolean schemaVersionIsR10(final Connection connection) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final String sql = "select count(1) is_r10 from admdbversion where dbv_phpversion like '10.02%'";

		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("is_r10") > 0;
			}
		} catch (final SQLException e) {
			AppLogger.getLogger().error("Error on get version of connection from admdbversion", e);
		} finally {
			try {
				ps.close();
			} catch (final SQLException e) {
			}
			try {
				rs.close();
			} catch (final SQLException e) {
			}
		}

		return false;
	}
}
