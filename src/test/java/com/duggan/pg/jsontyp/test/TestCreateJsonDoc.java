package com.duggan.pg.jsontyp.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.Document;

public class TestCreateJsonDoc {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void create(){
		String docTypeName = "KE.CO.WORKPOINT.PAYMENT.PAYMENTREQUEST";
		Document doc = new Document();
		doc.setType(DocumentDaoHelper.getDocumentType(docTypeName));
		DocumentDaoHelper.createJson(doc);
	}
	
	
	@After
	public void commit() throws IOException {
		DB.commitTransaction();
		DB.closeSession();
	}
}
