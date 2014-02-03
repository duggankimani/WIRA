package com.duggan.workflow.test.formbuilder;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.KeyValuePair;

public class TestFormBuilder {

	FormDaoImpl dao;
	
	@Before
	public void setup() {
		DBTrxProvider.init();
		DB.beginTransaction();
		dao = DB.getFormDao();
	}
	
	@Test
	public void getField(){
		Field field = FormDaoHelper.getField(108L);
		
		List<KeyValuePair> pairs = field.getSelectionValues(); 
		
		Assert.assertNotNull(pairs);
		
		Assert.assertEquals(pairs.size(),3);
		
		System.err.println("Values = "+pairs);
	}
	
	@Ignore
	public void getForm(){
		Long value = DB.getDocumentDao().getFormId(3L);
		Assert.assertNotNull(value);
	}
	
	@Ignore
	public void createForm(){
		ADForm form = new ADForm();
		//form.setId(5L);
		form.setCaption("Create Customer Form1");
		form.setName("customer.create");
		
		ADProperty property = new ADProperty();
		property.setCaption("Height");
		property.setType(DataType.STRING);
		property.setName("height");
		form.addProperty(property);
		
		ADValue value = new ADValue();
		value.setStringValue("200px");
		property.setValue(value);
		
		ADField field = new ADField();
		field.setCaption("Title");
		field.setName("title");
		
		ADValue fieldValue = new ADValue();
		fieldValue.setStringValue("Invoice no/ Unique doc Identifier");
		field.setValue(fieldValue);
		
		form.addField(field);
		
		dao.save(form);
		
		Assert.assertNotNull(form.getId());

	}
	
	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProvider.close();
	}
}
