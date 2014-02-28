package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.Document;

public class BTMTrxTest {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void persist(){
		ADDocType docType = DB.getDocumentDao().getDocumentTypeById(1L);
		String subject = DB.getDocumentDao().generateDocumentSubject(docType);
		System.out.println(subject);
	}
	
	@After
	public void destroy(){
		DB.rollback();
		DBTrxProvider.close();
	}
}
