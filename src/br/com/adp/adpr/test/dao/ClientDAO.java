package br.com.adp.adpr.test.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.adp.adpr.test.model.Client;
import br.com.adp.adpr.test.util.TimeKeeper;

public class ClientDAO extends BaseDAO {

	public Map<String, Client> getClientsList() throws SQLException {
		final Map<String, Client> clientsList = new HashMap<String, Client>();

		final StringBuilder sql = this.getLoadSQL();

		this.LOG.debug(sql.toString());

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Client client = null;

		try {
			pstmt = this.connection.prepareStatement(sql.toString());
			pstmt.setString(1, "t");

			final long start = TimeKeeper.sysDate();

			rs = pstmt.executeQuery();

			TimeKeeper.logElapsedTime(this.LOG, start);

			while (rs.next()) {
				client = new Client();
				client = Client.fromResultSet(rs);

				clientsList.put(client.getId(), client);
			}

		} catch (final SQLException e) {
			this.cleanup(rs, pstmt);
			this.LOG.error(e.getMessage());
			throw e;
		} finally {
			this.cleanup(rs, pstmt);
		}

		return clientsList;
	}

	protected StringBuilder getLoadSQL() {
		final StringBuilder sql = new StringBuilder();

		sql.append(this.formatHeader(this.getClass().getSimpleName(), "getLoadSQL"));

		sql.append("SELECT *");
		sql.append(" FROM catclient ");
		sql.append("WHERE client_active = ?");

		return sql;
	}

}
