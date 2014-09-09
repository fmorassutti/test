package br.com.adp.adpr.test.util;

/**
 * @author $Author: rossetto $
 * @id $Id: RsHelper.java,v 1.14.6.1 2014/04/02 22:26:39 rossetto Exp $
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

public class RsHelper {

	public static Boolean retrieveBoolean(final ResultSet rs, final String string) throws SQLException {
		return StringUtils.defaultString(rs.getString(string)).equals("t");
	}

	public static String retrieveString(final ResultSet rs, final String string) throws SQLException {
		return rs.getString(string);
	}

	public static Integer retrieveInt(final ResultSet rs, final String fieldName) throws SQLException {
		Integer result = null;
		final String dbValue = rs.getString(fieldName);
		if (dbValue != null && !"".equals(dbValue)) {
			result = Integer.valueOf(dbValue);
		}
		return result;
	}

	public static Timestamp retrieveTimestamp(final ResultSet rs, final String string) throws SQLException {
		return rs.getTimestamp(string);
	}

	public static Date retrieveDate(final ResultSet rs, final String string) throws SQLException {
		return rs.getDate(string);
	}

	public static Long retrieveLong(final ResultSet rs, final String fieldName) throws SQLException {
		Long result = null;
		final String dbValue = rs.getString(fieldName);
		if (dbValue != null && !"".equals(dbValue)) {
			result = Long.valueOf(dbValue);
		}
		return result;
	}

	public static String blobToString(final ResultSet rs, final String string) throws SQLException, IOException {
		String result = null;
		final Blob blob = rs.getBlob(string);
		if (blob != null) {
			final BufferedReader in = new BufferedReader(new InputStreamReader(blob.getBinaryStream()));
			result = in.readLine();
		}

		return result;
	}

	public static Float retrieveFloat(final ResultSet rs, final String attribute) throws SQLException {
		Float result = null;
		final String dbValue = rs.getString(attribute);
		if (dbValue != null && !"".equals(dbValue)) {
			result = Float.valueOf(dbValue);
		}
		return result;

	}

	public static void closeResultSet(final ResultSet resultSet) throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}
	}

	public static Double retrieveDouble(final ResultSet rs, final String string) throws SQLException {
		return rs.getDouble(string);
	}

	public static Boolean isSet(final ResultSet rs, final String columnLabel) {
		Boolean result = true;
		try {
			rs.findColumn(columnLabel.toUpperCase());
		} catch (final SQLException e) {
			result = false;
		}
		return result;

	}
}
