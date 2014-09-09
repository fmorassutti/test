package br.com.adp.adpr.test.bo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.adp.adpr.test.dao.ClientDAO;
import br.com.adp.adpr.test.model.Client;
import br.com.adp.adpr.test.util.AppLogger;

public class ClientBO {

	public Map<String, Client> getClientsList() throws SQLException {
		final Logger logger = AppLogger.getLogger();
		final ClientDAO clientDAO = new ClientDAO();
		Map<String, Client> clientsList = new HashMap<String, Client>();

		try {
			clientsList = clientDAO.getClientsList();
		} catch (final SQLException e) {
			logger.error(e.getMessage());
			throw e;
		}

		return clientsList;
	}

}
