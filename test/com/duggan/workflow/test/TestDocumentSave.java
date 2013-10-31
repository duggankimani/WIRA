package com.duggan.workflow.test;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.StringValue;

public class TestDocumentSave {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void save(){
		Document doc = new Document();
		doc.setSubject("INV/89007/13");
		doc.setDescription("Invoice for .. .. .. ");
		doc.setDocumentDate(new Date());
		doc.setType(DocumentDaoHelper.getDocumentType("INVOICE"));
		doc.setValue("subject", new StringValue(null, "subject", doc.getSubject()));
		doc.setValue("description", new StringValue(null, "description", doc.getDescription()));
		doc.setValue("docDate", new DateValue(null, "docDate", new Date()));
		
		DocumentDaoHelper.save(doc);
	}
	
	@After
	public void close(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
