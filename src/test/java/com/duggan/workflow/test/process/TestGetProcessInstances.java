package com.duggan.workflow.test.process;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.ProcessLog;

public class TestGetProcessInstances {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.init();
	}
	
	@Test
	public void getAllProcessIntances(){
		List<ProcessLog> logs = DB.getProcessDao().getProcessInstances(null);
		for(ProcessLog log: logs){
			System.out.println(log);
		}
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
