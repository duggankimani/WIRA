package com.duggan.workflow.test.bpm;

import java.util.List;

import org.drools.definition.process.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

public class TestGetApprovalNodes {

	@Before
	public void setup(){
		DBTrxProvider.init();
		LoginHelper.get();
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
		long processInstanceId =  121L;
		List<Node> nodes = JBPMHelper.get().getProcessDia(processInstanceId);
		
		for(Node node: nodes){
			System.err.println("############ "+node.getName());
		}
		
	}

	@After
	public void tearDown(){
		try{
			LoginHelper.get().close();
		}catch(Exception e){}
		DBTrxProvider.close();		
	}
}
