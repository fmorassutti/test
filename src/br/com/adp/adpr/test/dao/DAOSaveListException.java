package br.com.adp.adpr.test.dao;

/**
 * @author $Author: fernando $
 * @version $Id: DAOSaveListException.java,v 1.2 2012/04/26 22:10:17 fernando Exp $
 */
public class DAOSaveListException extends Exception {

	private static final long serialVersionUID = 6799372419835723500L;

	public DAOSaveListException(final Throwable cause) {
		super(cause);
	}

	public DAOSaveListException(final String msg) {
		super(msg);
	}

	public DAOSaveListException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
