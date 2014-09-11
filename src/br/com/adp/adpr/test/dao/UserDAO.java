package br.com.adp.adpr.test.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.adp.adpr.test.model.User;
import br.com.adp.adpr.test.util.TimeKeeper;

public class UserDAO extends BaseDAO {

	public Map<Integer, User> getUsersList() throws SQLException {
		final Map<Integer, User> usersList = new LinkedHashMap<Integer, User>();

		final StringBuilder sql = this.getLoadSQL();

		this.LOG.debug(sql.toString());

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;

		try {
			pstmt = this.connection.prepareStatement(sql.toString());
			//pstmt.setString(1, "t");

			final long start = TimeKeeper.sysDate();

			rs = pstmt.executeQuery();

			TimeKeeper.logElapsedTime(this.LOG, start);

			while (rs.next()) {
				user = new User();
				user = User.fromResultSet(rs);

				usersList.put(user.getId(), user);
			}

		} catch (final SQLException e) {
			this.cleanup(rs, pstmt);
			this.LOG.error(e.getMessage());
			throw e;
		} finally {
			this.cleanup(rs, pstmt);
		}

		return usersList;
	}

	protected StringBuilder getLoadSQL() {
		final StringBuilder sql = new StringBuilder();

		sql.append(this.formatHeader(this.getClass().getSimpleName(), "getLoadSQL"));

		sql.append("SELECT *");
		sql.append(" FROM users ");
		sql.append("ORDER BY id ASC");

		return sql;
	}

}
