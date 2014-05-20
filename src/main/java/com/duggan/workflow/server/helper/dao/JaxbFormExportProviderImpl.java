package com.duggan.workflow.server.helper.dao;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;

public class JaxbFormExportProviderImpl implements ContextResolver<JAXBContext> {

	JAXBContext context;
	
	@Override
	public JAXBContext getContext(Class<?> arg0) {

		if(context!=null){
			return context;
		}
		
		try{
			context = JAXBContext.newInstance("com.duggan.workflow.server.dao.model");
			return context;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}
