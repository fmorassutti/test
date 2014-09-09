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
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.adp.adpr.test.exception.ConnectionError;
import br.com.adp.adpr.test.util.AppLogger;
import br.com.adp.adpr.test.util.ConnectionHelper;
import br.com.adp.adpr.test.util.RsHelper;
import br.com.adp.adpr.test.util.StatementHelper;
import br.com.adp.adpr.test.util.TimeKeeper;


/**
 * @author $Author: rossetto $
 * @version $Id: BaseDAO.java,v 1.13.2.2 2013/08/14 21:29:21 rossetto Exp $
 */
public class BaseDAO {

	protected static final String HEADER = "/* FACADE=BaseDAO EP=Test DAO={dao} DAOSQL={operation} */";

	protected static final String SEMI_JOIN_CHOOSE = "choose";

	protected static final String SEMI_JOIN_OFF = "off";

	protected transient Connection connection;

	private transient final Map<String, PreparedStatement> baseDAOStatements = new HashMap<String, PreparedStatement>();

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
			this.connection = ConnectionHelper.getConnection();
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
		sql.append("select dbv_phpversion from (select dbv_phpversion from admdbversion dbv order by dbv.dbv_phpversion desc) where rownum = 1 ");

		this.LOG.debug(sql.toString());

		PreparedStatement pstmt = null; // NOPMD by PesaF on 24/04/12 11:05
		ResultSet resultSet = null; // NOPMD by PesaF on 24/04/12 11:04

		String version = "N/A"; // NOPMD by PesaF on 24/04/12 11:07

		try {
			pstmt = this.connection.prepareStatement(sql.toString());

			final long start = TimeKeeper.sysDate();

			resultSet = pstmt.executeQuery();

			this.LOG.debug(TimeKeeper.getElapsedTime(start));

			while (resultSet.next()) {
				version = RsHelper.retrieveString(resultSet, "dbv_phpversion"); // NOPMD by PesaF on 24/04/12 11:07
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

	public String getSequenceValue(final String sequence) throws SQLException {
		final StringBuilder sql = new StringBuilder();

		/* Build SQL Headers */
		sql.append(this.formatHeader(this.getClass().getSimpleName(), "getSequenceValue"));

		/* Build the query */
		sql.append("select " + sequence + " next_val from dual");

		this.LOG.debug(sql.toString());

		PreparedStatement pstmt = null; // NOPMD by PesaF on 24/04/12 11:07
		ResultSet resultSet = null; // NOPMD by PesaF on 24/04/12 11:05

		String nextVal = null; // NOPMD by PesaF on 24/04/12 11:09

		try {
			pstmt = this.connection.prepareStatement(sql.toString());

			final long start = TimeKeeper.sysDate();

			resultSet = pstmt.executeQuery();

			this.LOG.debug(TimeKeeper.getElapsedTime(start));

			while (resultSet.next()) {
				nextVal = RsHelper.retrieveString(resultSet, "next_val"); // NOPMD by PesaF on 24/04/12 11:09
			}

		} catch (final SQLException e) {
			this.cleanup(resultSet, pstmt);
			this.LOG.error(e.getMessage(), e);
			throw e;
		} finally {
			this.cleanup(resultSet, pstmt);
		}

		return nextVal;
	}

	/**
	 * Get a list of sequence values in a single DB call
	 * 
	 * @param sequence The name of the sequence from the SequenceHelper
	 * @param valuesCount The number of values to be generated
	 * @return The ResultSet with the list of values - IT MUST BE CLOSED BY THE CALLER!
	 * @throws SQLException
	 */
	public ResultSet getSequenceValues(final String sequence, final Integer valuesCount) throws SQLException {
		final long start = TimeKeeper.sysDate();
		final String statementName = "getSequenceValues";
		ResultSet resultSet = null; // NOPMD by PesaF on 24/04/12 11:05

		/* Get the statement from the object cache */
		PreparedStatement pstmt = this.baseDAOStatements.get(statementName);

		if (pstmt == null) {
			this.LOG.debug("Creating the prepared statement: " + statementName);
			/* Build SQL Headers */
			final StringBuilder sql = new StringBuilder();
			sql.append(this.formatHeader(this.getClass().getSimpleName(), statementName));
			/* Build the query */
			sql.append("select seqvalue.column_value seqval from table(pkg_migrationengine.get_sequence_values(?,?)) seqvalue");
			this.LOG.debug(sql.toString());
			/* Create the statement */
			pstmt = this.connection.prepareStatement(sql.toString());
			this.baseDAOStatements.put(statementName, pstmt);
		}

		try {
			StatementHelper.setString(pstmt, 1, sequence);
			StatementHelper.setInteger(pstmt, 2, valuesCount);
			resultSet = pstmt.executeQuery();
		} catch (final SQLException e) {
			this.LOG.error(e.getMessage(), e);
			throw e;
		}

		this.LOG.debug(TimeKeeper.getElapsedTime(start));
		return resultSet;
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
