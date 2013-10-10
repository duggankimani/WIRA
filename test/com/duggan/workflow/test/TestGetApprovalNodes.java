package com.duggan.workflow.test;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.NodeDetail;

public class TestGetApprovalNodes {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.init();
	}
	
	@Test
	public void getApprovalNodeStatus(){
		List<NodeDetail> lst = JBPMHelper.get().getWorkflowProcessDia(12L);
		
		System.err.println("List :: "+lst.size());
		//Assert.assertTrue(lst.size()==2);
		
	}
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
		DBTrxProvider.close();
	}
}
