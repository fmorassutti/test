package br.com.adp.adpr.test.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.adp.adpr.test.util.RsHelper;

/**
 * @author $Author: rossetto $
 * @version $Id: Client.java,v 1.40.2.3 2013/09/03 19:20:13 rossetto Exp $
 */

public class Client {

	private String id;

	private Integer maxReq;

	private String extensions;

	private String organizationOID;

	private String regionCode;

	private String loginSchema;

	private String configurations;

	private String logoFileExt;

	private Boolean active;

	private Integer customCount;

	private String tableExtensions;

	private String pod;

	private String orgName;

	private String resourceId;

	/**
	 * Indicates if the client is from NAS or MAS for determining the preferences defaults.
	 */
	private String clientDivision;

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Integer getMaxReq() {
		return this.maxReq;
	}

	public void setMaxReq(final Integer maxReq) {
		this.maxReq = maxReq;
	}

	public String getExtensions() {
		return this.extensions;
	}

	public void setExtensions(final String extensions) {
		this.extensions = extensions;
	}

	public String getOrganizationOID() {
		return this.organizationOID;
	}

	public void setOrganizationOID(final String organizationOID) {
		this.organizationOID = organizationOID;
	}

	public String getRegionCode() {
		return this.regionCode;
	}

	public void setRegionCode(final String regionCode) {
		this.regionCode = regionCode;
	}

	public String getLoginSchema() {
		return this.loginSchema;
	}

	public void setLoginSchema(final String loginSchema) {
		this.loginSchema = loginSchema;
	}

	public String getConfigurations() {
		return this.configurations;
	}

	public void setConfigurations(final String configurations) {
		this.configurations = configurations;
	}

	public String getLogoFileExt() {
		return this.logoFileExt;
	}

	public void setLogoFileExt(final String logoFileExt) {
		this.logoFileExt = logoFileExt;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Integer getCustomCount() {
		return this.customCount;
	}

	public void setCustomCount(final Integer customCount) {
		this.customCount = customCount;
	}

	public String getTableExtensions() {
		return this.tableExtensions;
	}

	public void setTableExtensions(final String tableExtensions) {
		this.tableExtensions = tableExtensions;
	}

	public String getPod() {
		return this.pod;
	}

	public void setPod(final String pod) {
		this.pod = pod;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(final String orgName) {
		this.orgName = orgName;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(final String resourceId) {
		this.resourceId = resourceId;
	}

	public String getClientDivision() {
		return this.clientDivision;
	}

	public void setClientDivision(final String clientDivision) {
		this.clientDivision = clientDivision;
	}

	public static Client fromResultSet(final ResultSet rsClient) throws SQLException {
		final Client client = new Client();

		if (RsHelper.isSet(rsClient, "client_code")) {
			client.setId(RsHelper.retrieveString(rsClient, "client_code"));
		}

		if (RsHelper.isSet(rsClient, "client_orgoid")) {
			client.setOrganizationOID(RsHelper.retrieveString(rsClient, "client_orgoid"));
		}

		if (RsHelper.isSet(rsClient, "client_region_code")) {
			client.setRegionCode(RsHelper.retrieveString(rsClient, "client_region_code"));
		}

		if (RsHelper.isSet(rsClient, "client_login_schema")) {
			client.setLoginSchema(RsHelper.retrieveString(rsClient, "client_login_schema"));
		}

		if (RsHelper.isSet(rsClient, "client_configurations")) {
			client.setConfigurations(RsHelper.retrieveString(rsClient, "client_configurations"));
		}

		if (RsHelper.isSet(rsClient, "client_active")) {
			client.setActive(RsHelper.retrieveBoolean(rsClient, "client_active"));
		}

		if (RsHelper.isSet(rsClient, "pod_code")) {
			client.setPod(RsHelper.retrieveString(rsClient, "pod_code"));
		}

		if (RsHelper.isSet(rsClient, "client_maxreq")) {
			client.setMaxReq(RsHelper.retrieveInt(rsClient, "client_maxreq"));
		}

		if (RsHelper.isSet(rsClient, "client_custom_count")) {
			client.setCustomCount(RsHelper.retrieveInt(rsClient, "client_custom_count"));
		}

		if (RsHelper.isSet(rsClient, "client_table_extensions")) {
			client.setTableExtensions(RsHelper.retrieveString(rsClient, "client_table_extensions"));
		}

		if (RsHelper.isSet(rsClient, "org_name")) {
			client.setOrgName(RsHelper.retrieveString(rsClient, "org_name"));
		}

		/* Used on R12+*/
		if (RsHelper.isSet(rsClient, "res_key")) {
			client.setResourceId(RsHelper.retrieveString(rsClient, "res_key"));
		}

		if (RsHelper.isSet(rsClient, "client_division")) {
			client.setClientDivision(RsHelper.retrieveString(rsClient, "client_division"));
		}
		return client;

	}

}
