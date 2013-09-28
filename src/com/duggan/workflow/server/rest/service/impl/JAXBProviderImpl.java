package com.duggan.workflow.server.rest.service.impl;

import javax.ws.rs.ext.ContextResolver;

import org.jbpm.executor.api.CommandContext;

import com.duggan.workflow.server.rest.model.BusinessKey;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.sun.jersey.api.json.JSONJAXBContext;

public class JAXBProviderImpl implements ContextResolver<JSONJAXBContext> {

	private JSONJAXBContext jsonJAXBContext;

	@Override
	public JSONJAXBContext getContext(Class<?> arg0) {

		if (!(arg0.equals(BusinessKey.class)
				|| arg0.equals(CommandContext.class)
				|| arg0.equals(Request.class) || arg0.equals(Response.class))) {
			return null;
		}

		if (jsonJAXBContext != null) {
			return jsonJAXBContext;
		}

		try {
			jsonJAXBContext = new JSONJAXBContext(BusinessKey.class,
					CommandContext.class, Request.class, Response.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return jsonJAXBContext;
	}
}