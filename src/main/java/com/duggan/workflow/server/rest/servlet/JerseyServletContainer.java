package com.duggan.workflow.server.rest.servlet;

import javax.ws.rs.ext.RuntimeDelegate;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class JerseyServletContainer extends ServletContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JerseyServletContainer() {
		super();
		RuntimeDelegate.setInstance(new   
				com.sun.jersey.server.impl.provider.RuntimeDelegateImpl());
		
	}
}
