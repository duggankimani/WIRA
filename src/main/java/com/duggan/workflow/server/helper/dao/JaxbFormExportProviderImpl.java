package com.duggan.workflow.server.helper.dao;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADKeyValuePair;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.ADProcessCategory;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.CatalogColumnModel;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.IntValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;

public class JaxbFormExportProviderImpl implements ContextResolver<JAXBContext> {

	JAXBContext context;
	
	@Override
	public JAXBContext getContext(Class<?> arg0) {

		if(context!=null){
			return context;
		}
		
		try{
			context = JAXBContext.newInstance(
					Form.class, Field.class, Property.class, 
					StringValue.class,DoubleValue.class,IntValue.class,BooleanValue.class,
					ProcessDefModel.class,TaskStepModel.class,ADTaskStepTrigger.class, ADTrigger.class, ADDocType.class, 
					ADProcessCategory.class,ADForm.class, ADOutputDoc.class, ADField.class, ADProperty.class,ADValue.class, 
					ADKeyValuePair.class, KeyValuePair.class, CatalogModel.class, CatalogColumnModel.class);
			return context;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}
