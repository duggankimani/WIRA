package com.duggan.workflow.server.dao.helper;

import javax.ws.rs.ext.ContextResolver;

import org.guvnor.common.services.project.model.Repository;

import com.sun.jersey.api.json.JSONJAXBContext;

public class KIEJaxbBinding implements ContextResolver<JSONJAXBContext> {

	private JSONJAXBContext jsonJAXBContext;

	@Override
	public JSONJAXBContext getContext(Class<?> arg0) {


		if (!(arg0.equals(Repository.class))) {
			return null;
		}

		if (jsonJAXBContext != null) {
			return jsonJAXBContext;
		}

		try {
			jsonJAXBContext = new JSONJAXBContext(Repository.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return jsonJAXBContext;
	}
}