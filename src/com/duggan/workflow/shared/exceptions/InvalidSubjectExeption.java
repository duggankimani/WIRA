package com.duggan.workflow.shared.exceptions;

/**
 * Thrown when subject provided already exists 
 * 
 * @author duggan
 *
 */
public class InvalidSubjectExeption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSubjectExeption(String message){
		super(message);
	}

}
