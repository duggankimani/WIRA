package com.duggan.workflow.test.process;

import org.jbpm.persistence.processinstance.ProcessInstanceInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Document;

public class TestLPOApproval {

	Document doc;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		doc = DocumentDaoHelper.getDocument(9L);
	}
	
	@Test
	public void execute(){
		JBPMHelper.get().createApprovalRequest("calcacuervo",doc);
		
	}
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
