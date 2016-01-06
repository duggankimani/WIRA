package com.duggan.workflow.test.export.html2pdf;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.form.Field;

public class TestLoadDynamicFields {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void load(){
		Doc doc = DocumentDaoHelper.getDocument(109L);
		Field field = FormDaoHelper.getField(175L);
		
		FormDaoHelper.loadFieldValues(doc, Arrays.asList(field));
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}
}
