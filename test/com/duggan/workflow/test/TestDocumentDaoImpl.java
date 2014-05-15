package com.duggan.workflow.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;

public class TestDocumentDaoImpl {

	DocumentDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		dao = DB.getDocumentDao();
	}
	
	@Test
	public void getAttachments(){
		long documentId = 81L;
		DB.getAttachmentDao().getHasAttachment(documentId);
	}
	
	@Ignore
	public void getDocType(){
		ADDocType type = dao.getDocumentTypeById(2L);
		Assert.assertNotNull(type);
	}
	
	@Ignore
	public void search(){
		Document model = DocumentDaoHelper.getDocument(60L);
		System.err.println(">>>>>>>> "+model.getDetails().size());
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
		 List<DocumentModel> models = dao.getAllDocuments(DocStatus.DRAFTED);
		 dao.getAllDocuments(DocStatus.APPROVED);
		 dao.getAllDocuments(DocStatus.INPROGRESS);
		 dao.getAllDocuments(DocStatus.REJECTED);
		 
		
		Assert.assertEquals(0,models.size());
	}
	
	@Ignore
	public void createDocument(){
//		DocumentModel doc = new DocumentModel(null,
//				"Test", "Test", DocumentType.INVOICE);
//		
//		DocumentModel model = dao.saveDocument(doc);
//		Assert.assertNotNull(model.getId());
//
//		List<DocumentModel> docs = dao.getAllDocuments(DocStatus.DRAFTED);
//		Assert.assertEquals(1, docs.size());
//		
//		dao.delete(doc);
//		
//		docs = dao.getAllDocuments(DocStatus.DRAFTED);
//		Assert.assertEquals(0, docs.size());
		
		
	}
	
	@Ignore
	public void createDocumentHelper(){
		
		Document doc = new Document();
		doc.setCreated(new Date());
		doc.setType(DocumentDaoHelper.getType(DB.getDocumentDao().getDocumentTypeById(1L)));
		doc.setDescription("test");
		doc.setDocumentDate(new Date());
		//doc.setType(DocumentType.INVOICE);
		doc.setSubject("test");
		
		Document model = DocumentDaoHelper.save(doc);
		Assert.assertNotNull(model.getId());

		List<Doc> docs = DocumentDaoHelper.getAllDocuments(DocStatus.DRAFTED);
		Assert.assertEquals(1, docs.size());
		
//		dao.delete(doc.getId());
//		
//		docs = DocumentDaoHelper.getAllDocuments();
//		Assert.assertEquals(0, docs.size());
//		
		
	}

	
	@After
	public void close(){
		DB.rollback();
		DB.closeSession();
	}
	
}
