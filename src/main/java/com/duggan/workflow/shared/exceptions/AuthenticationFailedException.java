package com.duggan.workflow.shared.exceptions;

public class AuthenticationFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String cause;
	
	public AuthenticationFailedException(String msg, Exception e){
		super(msg, e);
	}
}
