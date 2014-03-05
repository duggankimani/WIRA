package com.duggan.workflow.server.rest.exception;

public class CommandNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandNotFoundException(String msg) {
		super(msg);
	}
}
