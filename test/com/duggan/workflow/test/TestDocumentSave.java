package com.duggan.workflow.test;

import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.IntValue;
import com.duggan.workflow.shared.model.StringValue;

public class TestDocumentSave {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Ignore
	public void save(){
		Document doc = new Document();
		doc.setSubject("INV/99004/13");
		doc.setDescription("Invoice for .. .. .. ");
		doc.setDocumentDate(new Date());
		doc.setType(DocumentDaoHelper.getDocumentType("INVOICE"));
		doc.setValue("subject", new StringValue(null, "subject", doc.getSubject()));
		doc.setValue("description", new StringValue(null, "description", doc.getDescription()));
		doc.setValue("docDate", new DateValue(null, "docDate", new Date()));
		
		DocumentLine line = new DocumentLine();
		line.setName("invoiceline");
		line.addValue("itemno", new IntValue(null, "itemno", 1));
		line.addValue("desc", new StringValue(null, "desc", "Consultancy services"));
		line.addValue("total", new StringValue(null, "total", "200,000Ksh"));
		doc.addDetail(line);
		
		DocumentLine line2 = new DocumentLine();
		line2.setName("invoiceline");
		line2.addValue("itemno", new IntValue(null, "itemno", 2));
		line2.addValue("desc", new StringValue(null, "desc", "Coding services"));
		line2.addValue("total", new StringValue(null, "total", "100,000Ksh"));
		doc.addDetail(line2);
		
		doc =DocumentDaoHelper.save(doc);
		
		Assert.assertNotNull(doc.getDetails());
		Assert.assertEquals(1, doc.getDetails().size());
		Assert.assertEquals(2,doc.getDetails().get("invoiceline").size());
		Assert.assertEquals(3,doc.getDetails().get("invoiceline").get(0).getValues().size());
		
	}
	
	@After
	public void close(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
