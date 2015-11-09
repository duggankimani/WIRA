package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;

public class TestGetApprovalNodes {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(5L);
	}
	
	@Test
	public void getApprovalNodeStatus(){
		
		Long processInstanceId = 1116L;
		//JBPMHelper.get().upgradeProcessInstance(processInstanceId, "org.im.v2.AssetFinance");
		
		//JBPMHelper.get().getNode(task);
		
//		List<NodeDetail> lst = JBPMHelper.get().getWorkflowProcessDia(processInstanceId);
//		System.err.println("List :: "+lst.size());
		
		//Assert.assertTrue(lst.size()==2);
	
//		WorkflowProcessInstanceUpgrader.upgradeProcessInstance(kruntime,
//				processInstanceId, processId, nodeMapping);
	}
	
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
		DBTrxProviderImpl.close();
	}
}
