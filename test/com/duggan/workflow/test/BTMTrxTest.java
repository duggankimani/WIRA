package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.shared.model.Document;

public class BTMTrxTest {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void persist(){
		Document doc = DocumentDaoHelper.getDocument(31);
		String desc = "#####try - 4";
		doc.setDescription(desc);
		DocumentDaoHelper.save(doc);
		DB.rollback();
		
		//new transaction
		DB.beginTransaction();
		Document saved = DocumentDaoHelper.getDocument(31);
		Assert.assertNotSame(desc, saved.getDescription());
		DB.commitTransaction();
	}
	
	@After
	public void destroy(){
		
		DBTrxProvider.close();
	}
}
