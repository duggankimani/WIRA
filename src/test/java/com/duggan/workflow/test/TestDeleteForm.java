package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.Property;

public class TestDeleteForm {

	FormDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		dao = DB.getFormDao();
	}
	
	@Test
	public void delete(){
		Form form = FormDaoHelper.getForm(47L, true);
		Property p=form.getProperties().get(1);
		Value v = p.getValue();
		
		Assert.assertNotNull(v);
		Assert.assertNotNull(v.getValue());
		System.err.println(p.getCaption()+" :: "+v.getValue());
		//FormDaoHelper.delete(form);
	}
	
	@After
	public void close(){
		DB.commitTransaction();
		DB.closeSession();
	}

}
