package com.duggan.workflow.shared.exceptions;

public class InitializationFailureError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitializationFailureError(String msg, Exception cause){
		super(msg, cause);
	}
}
