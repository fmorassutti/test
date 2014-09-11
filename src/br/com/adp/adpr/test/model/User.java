package br.com.adp.adpr.test.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.adp.adpr.test.util.RsHelper;

public class User {

	private int id;
	
	private String name;
	
	private String login;
	
	private String password;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public static User fromResultSet(final ResultSet rsUser) throws SQLException {
		final User user = new User();

		if (RsHelper.isSet(rsUser, "id")) {
			user.setId(RsHelper.retrieveInt(rsUser, "id"));
		}
		
		if (RsHelper.isSet(rsUser, "name")) {
			user.setName(RsHelper.retrieveString(rsUser, "name"));
		}

		if (RsHelper.isSet(rsUser, "login")) {
			user.setLogin(RsHelper.retrieveString(rsUser, "login"));
		}
		
		if (RsHelper.isSet(rsUser, "password")) {
			user.setPassword(RsHelper.retrieveString(rsUser, "password"));
		}
		
		return user;

	}
	
}
