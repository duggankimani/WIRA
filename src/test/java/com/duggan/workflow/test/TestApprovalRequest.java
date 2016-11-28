package com.duggan.workflow.test;

import java.io.IOException;
import java.util.List;

import org.apache.http.entity.mime.content.ContentBody;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.TaskType;

public class TestApprovalRequest {

	Integer documentId=5;
	String userId="calcacuervo";
	
	DocumentDaoImpl dao;
	
	ContentBody l;
	
	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		
		DB.beginTransaction();
		dao = DB.getDocumentDao();
	}
	
	@Test
	public void submit(){
		//Document document = DocumentDaoHelper.getDocument(documentId);
		
		//HTSummary doc = document.toTask();
		Document doc = DocumentDaoHelper.getDocument(10L);
		JBPMHelper.get().createApprovalRequest("calcacuervo",doc);
		
		List<HTSummary> lst = JBPMHelper.get().getTasksForUser(null,userId, TaskType.INBOX,0,100);
		
		for(HTSummary summary: lst){
			System.err.println(summary.getDocumentRef()+" : "+summary.getCaseNo()+" : "+summary.getDescription());
		}
		
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
	
}
