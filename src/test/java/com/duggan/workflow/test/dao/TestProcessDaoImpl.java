package com.duggan.workflow.test.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.CaseFilter;
import com.duggan.workflow.shared.model.ProcessLog;

public class TestProcessDaoImpl {
	
	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(2L);
	}
	
	@Test
	public void getNextAssignee(){
		String nextAssignee = DB.getProcessDao().getNextAssignee(113L, "ClaimVerification"
				,"chasebank.finance.ExpenseClaim", Arrays.asList("Admin", "Finance"));
		System.err.println(nextAssignee);
	}
	
	@Ignore
	public void loadProcessMetadata(){
		JBPMHelper.get().getWorkflowProcessNodes("chasebank.finance.ExpenseClaim");
	}

	@Ignore
	public void getProcessRegistry(){
		CaseFilter filter = new CaseFilter();
		filter.setUserId("Administrator");
		List<ProcessLog> logs = DB.getProcessDao().getProcessInstances(filter, null, null);
		System.err.println("Logs = "+logs.size());
	}
	
	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProviderImpl.close();
	}

}

