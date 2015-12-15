package com.duggan.workflow.test.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.CaseFilter;
import com.duggan.workflow.shared.model.ProcessLog;

public class TestProcessDaoImpl {
	
	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}

	@Test
	public void getProcessRegistry(){
		CaseFilter filter = new CaseFilter();
		filter.setUserId("Administrator");
		List<ProcessLog> logs = DB.getProcessDao().getProcessInstances(filter);
		System.err.println("Logs = "+logs.size());
	}
	
	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProviderImpl.close();
	}

}

