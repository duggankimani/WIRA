package com.duggan.workflow.test.bpm;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.NodeDetail;

public class TestGetApprovalNodes {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		ProcessMigrationHelper.init();
	}
	
	@Test
	public void getApprovalStatus(){
		/*
		 * Approach 1 - Get all Tasks generated & their statuses
		 * and use that as the approval log (Match approvers & not approval nodes)
		 */
		
		/*
		 *Approach 2 - Get all completed Human Task nodes 
		 */	
		//long processInstanceId =  118L;
		long processInstanceId =  133L;
//		List<Node> nodes = JBPMHelper.get().getProcessDia(processInstanceId);
//		
//		for(Node node: nodes){
//			System.err.println("############ "+node.getName());
//		}
		List<NodeDetail> nodes = JBPMHelper.get().getWorkflowProcessDia(processInstanceId);
		System.err.println(nodes);
	}

	@After
	public void tearDown(){
		DBTrxProviderImpl.close();		
	}
}
