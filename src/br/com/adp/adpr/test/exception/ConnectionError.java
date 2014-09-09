/*
 * ConnectionError.java
 *
 *
 * Copyright (c) 2009 Automatic Data Processing, Inc.
 * 1 ADP Boulevard, Roseland, New Jersey, 07068, U.S.A.
 * All rights reserved.
 */
package br.com.adp.adpr.test.exception;

/**
 * @author $Author: fernando $
 * @version $Id: ConnectionError.java,v 1.2 2012/04/11 17:40:25 fernando Exp $
 */
public class ConnectionError extends Exception {

	private static final long serialVersionUID = -866214711740100338L;

	public ConnectionError() {
		super();
	}

	public ConnectionError(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ConnectionError(final Throwable cause) {
		super(cause);
	}

	public ConnectionError(final String msg) {
		super(msg);
	}
}
