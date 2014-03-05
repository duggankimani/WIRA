package com.duggan.workflow.server.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class WiraServiceException extends WebApplicationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WiraServiceException(WiraExceptionModel ex){
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON)
				.entity(ex).build());
	}
}
