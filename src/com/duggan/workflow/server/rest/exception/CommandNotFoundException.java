package com.duggan.workflow.server.rest.exception;

import com.duggan.workflow.server.rest.model.Request;

public class CommandNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandNotFoundException(Request request) {
		super("Could not find command : "+request);
	}
}
