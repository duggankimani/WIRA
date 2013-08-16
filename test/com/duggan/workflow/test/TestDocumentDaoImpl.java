package com.duggan.workflow.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.github.gwtbootstrap.client.ui.constants.AlertType;

public class TestDocumentDaoImpl {

	DocumentDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		dao = DB.getDocumentDao();
	}
	
	@Ignore
	public void getDocType(){
		DocType type = dao.getDocumentType(2L);
		Assert.assertNotNull(type);
	}
	
	@Test
	public void search(){
		Document model = DocumentDaoHelper.getDocument(3L);
	}
	
	@Ignore
	public void getCount(){
		HashMap<TaskType, Integer> counts = new HashMap<TaskType, Integer>();
		DocumentDaoHelper.getCounts(counts);
		
		for(TaskType key: counts.keySet()){
			System.err.println(key+" : "+counts.get(key));
		}
	}
	
	@Ignore
	public void getEM(){
		Assert.assertNotNull(dao);
		
		Assert.assertEquals(0, dao.getAllDocuments(DocStatus.DRAFTED).size());
	}
	
	@Ignore
	public void createDocument(){
		DocumentModel doc = new DocumentModel(null,
				"Test", "Test", DocType.INVOICE);
		
		DocumentModel model = dao.saveDocument(doc);
		Assert.assertNotNull(model.getId());

		List<DocumentModel> docs = dao.getAllDocuments(DocStatus.DRAFTED);
		Assert.assertEquals(1, docs.size());
		
		dao.delete(doc);
		
		docs = dao.getAllDocuments(DocStatus.DRAFTED);
		Assert.assertEquals(0, docs.size());
		
		
	}
	
	@Ignore
	public void createDocumentHelper(){
		
		Document doc = new Document();
		doc.setCreated(new Date());
		doc.setDescription("test");
		doc.setDocumentDate(new Date());
		doc.setType(DocType.INVOICE);
		doc.setSubject("test");
		
		Document model = DocumentDaoHelper.save(doc);
		Assert.assertNotNull(model.getId());

		List<DocSummary> docs = DocumentDaoHelper.getAllDocuments(DocStatus.DRAFTED);
		Assert.assertEquals(1, docs.size());
		
//		dao.delete(doc.getId());
//		
//		docs = DocumentDaoHelper.getAllDocuments();
//		Assert.assertEquals(0, docs.size());
//		
		
	}

	
	@After
	public void close(){
		DB.closeSession();
	}
	
}
