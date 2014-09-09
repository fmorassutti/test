package br.com.adp.adpr.test.dao;

/**
 * @author $Author: fernando $
 * @Id $Id: DAOSaveException.java,v 1.2 2012/04/26 22:10:17 fernando Exp $
 */
public class DAOSaveException extends Exception {

	public DAOSaveException(final Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -2978487402705189397L;

	public DAOSaveException(final String msg) {
		super(msg);
	}

	public DAOSaveException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
