/*
 * ConnectionHelper.java
 *
 *
 * Copyright (c) 2009 Automatic Data Processing, Inc.
 * 1 ADP Boulevard, Roseland, New Jersey, 07068, U.S.A.
 * All rights reserved.
 */
package br.com.adp.adpr.test.util;

import java.sql.*;

import br.com.adp.adpr.test.exception.ConnectionError;

public interface ConnectionHelperInterface {

	public static ThreadLocal<Connection> connection = new ThreadLocal<Connection>();

	public static String JDBC_DRIVER_STRING = "";

	/**
	 * This method create an oracle database connection.
	 * @return 
	 *
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws ConnectionError 
	 */
	abstract void initializeConnectionHelper() throws SQLException, ClassNotFoundException, ConnectionError;

	/**
	 * Getter
	 *
	 * @return
	 * @throws ConnectionError
	 * @throws SQLException
	 * @throws DatabaseException
	 */
	abstract Connection getConnection() throws ConnectionError;

	abstract void closeConnection();

}
