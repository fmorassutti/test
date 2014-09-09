package br.com.adp.adpr.test.util;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

/**
 * Ajudande GAUCHO 2.0
 * @author RossettM
 *
 */
public class StatementHelper {

	public static void setBoolean(final PreparedStatement pstmt, final Integer parameterIndex, final Boolean value) throws SQLException {
		if (value == null || Boolean.toString(value) == null || Boolean.toString(value) == "") {
			pstmt.setNull(parameterIndex, java.sql.Types.VARCHAR);
		} else {
			pstmt.setString(parameterIndex, value ? "t" : "f");
		}
	}

	public static void setString(final PreparedStatement pstmt, final Integer parameterIndex, final String value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.VARCHAR);
		} else {
			pstmt.setString(parameterIndex, value);
		}
	}

	public static void setInteger(final PreparedStatement pstmt, final Integer parameterIndex, final Integer value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.INTEGER);
		} else {
			pstmt.setInt(parameterIndex, value);
		}
	}

	public static void setInteger(final PreparedStatement pstmt, final Integer parameterIndex, final String value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.INTEGER);
		} else {
			pstmt.setInt(parameterIndex, Integer.parseInt(value));
		}
	}

	public static void setDate(final PreparedStatement pstmt, final Integer parameterIndex, final Date value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.DATE);
		} else {
			pstmt.setDate(parameterIndex, value);
		}
	}

	public static void setTimestamp(final PreparedStatement pstmt, final Integer parameterIndex, final java.sql.Timestamp value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.TIMESTAMP);
		} else {
			pstmt.setTimestamp(parameterIndex, value);
		}
	}

	public static void setLong(final PreparedStatement pstmt, final Integer parameterIndex, final String value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.LONGNVARCHAR);
		} else {
			pstmt.setLong(parameterIndex, Long.parseLong(value));
		}
	}

	public static void setLong(final PreparedStatement pstmt, final Integer parameterIndex, final Long value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.LONGNVARCHAR);
		} else {
			pstmt.setLong(parameterIndex, value);
		}
	}

	public static void setNull(final PreparedStatement pstmt, final Integer parameterIndex, final int type) throws SQLException {
		pstmt.setNull(parameterIndex, type);

	}

	public static void setFloat(final PreparedStatement pstmt, final Integer parameterIndex, final Float value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.FLOAT);
		} else {
			pstmt.setFloat(parameterIndex, value);
		}
	}

	public static void setDouble(final PreparedStatement pstmt, final Integer parameterIndex, final Double value) throws SQLException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.DOUBLE);
		} else {
			pstmt.setDouble(parameterIndex, value);
		}

	}

	public static void stringToBlob(final PreparedStatement pstmt, final Integer parameterIndex, final String value) throws SQLException, IOException {
		if (value == null || "".equals(value)) {
			pstmt.setNull(parameterIndex, java.sql.Types.BLOB);
		} else {
			pstmt.setBinaryStream(parameterIndex, IOUtils.toInputStream(value, "UTF-8"));
		}
	}
}
