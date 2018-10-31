package com.duggan.workflow.test.document;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class TestDocumentDaoImpl {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void checkIfHasAttachment(){
		DB.getAttachmentDao().getHasAttachment(134L);
	}
	
	@Ignore
	public void testGetDocumentTypeByDocumentId(){
		
		DB.getDocumentDao().getDocumentTypeByDocumentId(100L);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		DB.closeSession();
	}
}
