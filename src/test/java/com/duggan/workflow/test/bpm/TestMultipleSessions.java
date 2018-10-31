package com.duggan.workflow.test.bpm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;

import junit.framework.Assert;

public class TestMultipleSessions {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
	}
	
	@Test
	public void testMultipleSessions(){

		Long documentId = 1L;
		String requestingUser = "calcacuervo";
		String approver1 = "mariano";
		Document document=null;
		
		//Start Workflow
		startTrx();		
		document = DocumentDaoHelper.getDocument(documentId);		
		JBPMHelper.get().createApprovalRequest(requestingUser, document);
		document = DocumentDaoHelper.getDocument(documentId);
		commitTrx();
		JBPMHelper.clearRequestData(); // Will dispose KSession
		
		//Start Process
		startTrx();
		List<HTSummary> summaries = JBPMHelper.get().getTasksForUser(null,approver1, document.getProcessInstanceId(),0,100);
		Assert.assertEquals(1, summaries.size());
		HTSummary summary = summaries.get(0);
		JBPMHelper.get().execute(summary.getId(), approver1, Actions.CLAIM, null);
		commitTrx();
		JBPMHelper.clearRequestData(); // Will dispose KSession

		//Complete process
		startTrx();
		JBPMHelper.get().execute(summary.getId(), approver1, Actions.START, null);
		commitTrx();
		JBPMHelper.clearRequestData(); // Will dispose KSession
	
		
		startTrx();
		JBPMHelper.get().execute(summary.getId(), approver1, Actions.SUSPEND, null);
		commitTrx();
		JBPMHelper.clearRequestData(); // Will dispose KSession
	
		
		startTrx();
		JBPMHelper.get().execute(summary.getId(), approver1, Actions.RESUME, null);
		commitTrx();
		JBPMHelper.clearRequestData(); // Will dispose KSession
	
		
		//Complete process
		startTrx();
		HashMap<String, Object> values = new HashMap<String, Object>(); 
		values.put("isApproved", false);
		values.put("documentId", documentId);
		JBPMHelper.get().execute(summary.getId(), approver1, Actions.COMPLETE, values);
		commitTrx();
		JBPMHelper.clearRequestData(); // Will dispose KSession
		
	}
	
	private void startTrx() {
		DB.beginTransaction();
	}


	private void commitTrx() {
		DB.commitTransaction();
		DB.closeSession();
	}
	
	@After
	public void tearDown() throws IOException{
	}
}
