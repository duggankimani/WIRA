package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

public class TestGetApprovalNodes {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void getApprovalNodeStatus(){
		JBPMHelper.get().getWorkflowProcessDia(13L);
	}
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
		DBTrxProvider.close();
	}
}
