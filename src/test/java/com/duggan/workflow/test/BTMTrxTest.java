package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class BTMTrxTest {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
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
		DBTrxProviderImpl.close();
	}
}
