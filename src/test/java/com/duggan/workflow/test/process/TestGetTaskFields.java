package com.duggan.workflow.test.process;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.form.ProcessMappings;

public class TestGetTaskFields {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.init();
	}
	
	@Test
	public void getNodeDetails(){
		ProcessMappings data = JBPMHelper.get().getProcessDataMappings("invoice-approval", "HODApproval");
		
		//System.err.println(params);
	}
	
	@Ignore
	public void getValues(){
		String processId = "defaultPackage.rfqprocess";
		String taskName = "SendRFQS";
		JBPMHelper.get().getProcessData(processId, taskName);
	}

	@After
	public void destroy(){
		
		DB.rollback();
		DB.closeSession();
	}
}
