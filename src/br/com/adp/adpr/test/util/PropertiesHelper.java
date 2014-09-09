package br.com.adp.adpr.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * @author $Author: rossetto $
 * @version $Id: PropertiesHelper.java,v 1.7.4.3 2013/12/02 17:51:03 rossetto Exp $
 */
public class PropertiesHelper {

	protected final Logger LOG = Logger.getLogger(PropertiesHelper.class);

	private static final String PROPERTY_FILE = "database.properties";

	public Properties props;

	public Properties getProps() {
		return this.props;
	}

	public void setProps(final Properties props) {
		this.props = props;
	}

	/** Private constructor 
	 * @throws IOException 
	 * @throws NamingException */
	public PropertiesHelper() throws NamingException, IOException {
		/* Read the properties file */
		this.init();
	}

	/**
	 * Read the property file and load it, this function is called by the
	 * constructor and the reload() method.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void init() throws FileNotFoundException, IOException, NamingException {
		this.LOG.info("Initializing PropertiesHelper Singleton: " + PROPERTY_FILE);

		try {
			this.setProps(new Properties());
			final Context ctx = new InitialContext();
			final URL url = (URL) ctx.lookup(PROPERTY_FILE);
			final InputStream input = url.openStream();
			this.getProps().load(input);
		} catch (final IOException e) {
			this.getProps().load(new FileInputStream("conf/" + PROPERTY_FILE));
		} catch (final NamingException e) {
			this.getProps().load(new FileInputStream("conf/" + PROPERTY_FILE));
		} catch (final Exception e) {
			this.getProps().load(new FileInputStream("conf/" + PROPERTY_FILE));
		}

	}

	/**
	 * Get a property value from the application properties file (PROPERTY_FILE)
	 * 
	 * @param prefix
	 *            The prefix property name
	 * @param key
	 *            The name of the property
	 * @param pDefaultValue
	 *            The default value to be returned in case there is any problem
	 * @return String (or pDefaultValue if not found)
	 */
	public String getPropertyByPrefixWithDefaultValue(final String pPrefix, final String pKey, final String pDefaultValue) {
		String retVal = null;
		String propName = null;

		if (this.getProps() == null) {

			/* Error reading properties file, returning default value */
			retVal = pDefaultValue;

		} else {

			/* Properties file is available! */
			String prefix = pPrefix;
			if (prefix == null) {
				prefix = "";
			}

			String key = pKey;
			if (key == null) {
				key = "";
			}

			propName = prefix.toLowerCase() + "." + key.toLowerCase();
			retVal = this.getProps().getProperty(propName);

			/* Check if property key was found */
			if (retVal == null && pDefaultValue != null) {
				retVal = pDefaultValue;
			}

			this.LOG.debug("Getting value for property: " + propName + " = " + retVal);

		}

		return retVal;
	}

	/**
	 * Get a property value from the application properties file (PROPERTY_FILE)
	 * 
	 * @param pProperty
	 * @return String (or null if not found)
	 */
	public String getProperty(final String pProperty) {
		return this.getProps().getProperty(pProperty);
	}

	/**
	 * Get a property value from the application properties file using a prefix
	 * 
	 * @param pPrefix
	 * @param pKey
	 * @return String (or null if not found)
	 */
	public String getPropertyByPrefix(final String pPrefix, final String pKey) {
		return this.getPropertyByPrefixWithDefaultValue(pPrefix, pKey, null);
	}

	/**
	 * Return <code>true</code> if the property file was found else returns
	 * <code>false</code>
	 * 
	 * @return
	 */
	public boolean isPropertiesAvailable() {
		boolean retVal = false;
		if (this.getProps() != null && !this.getProps().isEmpty()) {
			/* return true if the property file is not null and was loaded */
			retVal = true;
		}
		return retVal;
	}

	/**
	 * Clears the current properties and reloads property file
	 * @throws IOException 
	 * @throws NamingException 
	 */
	public void reload() throws NamingException, IOException {

		/* Clears the current properties array */
		this.setProps(null);
		/* Reload the properties file */
		this.init();
	}

	public String getFileName() {
		return PROPERTY_FILE;
	}

}