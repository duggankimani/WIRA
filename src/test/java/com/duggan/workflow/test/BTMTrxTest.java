package com.duggan.workflow.test;

import org.junit.Test;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.test.dao.AbstractDaoTest;

public class BTMTrxTest extends AbstractDaoTest{
	
	@Test
	public void persist(){
		ADDocType docType = DB.getDocumentDao().getDocumentTypeById(1L);
		String subject = DB.getDocumentDao().generateDocumentSubject(docType);
		System.out.println(subject);
	}
	
}
