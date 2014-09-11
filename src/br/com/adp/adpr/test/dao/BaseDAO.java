/*
 * BaseDao.java
 *
 *
 * Copyright (c) 2009 Automatic Data Processing, Inc.
 * 1 ADP Boulevard, Roseland, New Jersey, 07068, U.S.A.
 * All rights reserved.
 */
package br.com.adp.adpr.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import br.com.adp.adpr.test.exception.ConnectionError;
import br.com.adp.adpr.test.util.AppLogger;
import br.com.adp.adpr.test.util.MySqlConnectionHelper;
import br.com.adp.adpr.test.util.RsHelper;
import br.com.adp.adpr.test.util.TimeKeeper;

public class BaseDAO {

	protected static final String HEADER = "/* FACADE=BaseDAO EP=Test DAO={dao} DAOSQL={operation} */";

	protected transient Connection connection;

	protected final Logger LOG = AppLogger.getLogger();

	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * Build BaseDAO creating a new connection to the default database
	 */
	public BaseDAO() {
		this.init();
	}

	/**
	 * Build BaseDAO reusing existing connection
	 * @param dbConnection
	 */
	public BaseDAO(final Connection dbConnection) {
		//TODO Validate connection before assigning...
		this.connection = dbConnection;
	}

	private void init() {
		try {
			MySqlConnectionHelper helper = new MySqlConnectionHelper();
			this.connection = helper.getConnection();
		} catch (final ConnectionError e) {
			this.LOG.error(e.getMessage(), e);
		}
	}

	protected void cleanup(final ResultSet paramResultSet, final Statement paramStatement) {
		/* Closing the RS */
		if (paramResultSet != null) {
			try {
				paramResultSet.close();
			} catch (final SQLException e) {
				this.LOG.error(e.getMessage(), e);
			}
		}
		/* Closing the Statement */
		if (paramStatement != null) {
			try {
				paramStatement.close();
			} catch (final SQLException e) {
				this.LOG.error(e.getMessage(), e);
			}
		}
	}

	public String retrieveAppVersionFromDB() throws SQLException {
		final StringBuilder sql = new StringBuilder();

		/* Build SQL Headers */
		sql.append(this.formatHeader(this.getClass().getSimpleName(), "retrieveAppVersionFromDB"));

		/* Build the query */
		sql.append("select version from version order by date desc limit 1");

		this.LOG.debug(sql.toString());

		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		String version = "N/A";

		try {
			pstmt = this.connection.prepareStatement(sql.toString());

			final long start = TimeKeeper.sysDate();

			resultSet = pstmt.executeQuery();

			this.LOG.debug(TimeKeeper.getElapsedTime(start));

			while (resultSet.next()) {
				version = RsHelper.retrieveString(resultSet, "version");
			}

		} catch (final SQLException e) {
			this.cleanup(resultSet, pstmt);
			this.LOG.error(e.getMessage(), e);
			throw e;
		} finally {
			this.cleanup(resultSet, pstmt);
		}

		return version;
	}

	public void beginTransaction() throws SQLException {
		this.connection.setAutoCommit(false);
	}

	public void commitTransaction() throws SQLException {
		this.connection.commit();
	}

	public void rollbackTransaction() throws SQLException {
		this.connection.rollback();
	}

	/**
	 * This method inserts a header into the sql statement for auditing purposes. 
	 * @param daoName
	 * @param operation
	 * @return
	 */
	public String formatHeader(final String paramDAOName, final String paramOperation) {
		/* Defining the DAO name */
		String daoName;
		if (paramDAOName == null) {
			daoName = "N/A";
		} else {
			daoName = paramDAOName;
		}
		/* Defining the OPERATION name */
		String operation;
		if (paramOperation == null) {
			operation = "N/A";
		} else {
			operation = paramOperation;
		}
		/* Returning the SQL header */
		return BaseDAO.HEADER.replace("{dao}", daoName).replace("{operation}", operation);
	}

	/**
	 * This method retrieves the Database Charset Encoding. If null, use 'UTF-8' as default encoding
	 * 
	 * @return String Database Charset Encoding
	 * @throws SQLException
	 */
	public String retrieveDatabaseCharset() throws SQLException {

		String charset = null;

		/* Build SQL Headers */
		final StringBuilder sql = new StringBuilder(this.formatHeader(this.getClass().getSimpleName(), "retrieveDatabaseCharset"));

		sql.append("select value from nls_database_parameters where parameter = 'NLS_CHARACTERSET'");

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			this.LOG.info("Retrieving Database Charset Encoding...");

			stmt = this.connection.prepareStatement(sql.toString());

			final long start = TimeKeeper.sysDate();

			rs = stmt.executeQuery();

			this.LOG.info(TimeKeeper.getElapsedTime(start, "Retrieve Database Charset"));

			if (rs.next()) {
				charset = RsHelper.retrieveString(rs, "value");
			}

		} finally {
			this.cleanup(rs, stmt);
		}

		return charset;
	}

}
