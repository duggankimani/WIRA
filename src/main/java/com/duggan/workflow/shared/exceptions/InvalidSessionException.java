package com.duggan.workflow.shared.exceptions;

import java.io.Serializable;

import com.duggan.workflow.server.actionvalidator.SessionValidator;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Thrown by the {@link SessionValidator} whenever a valid session is found
 * 
 * @author duggan
 *
 */
public class InvalidSessionException extends ActionException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InvalidSessionException(){
	}
	
	public InvalidSessionException(String message) {
		super(message);
	}
}
