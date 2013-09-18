package com.duggan.workflow.test.bpm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;

public class MigrateProcess {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void start(){
		Long processDefId = 15L;
		
		ProcessMigrationHelper.start(processDefId);
	}
	
	@After
	public void tearDown(){
		DB.commitTransaction();
		DB.closeSession();
		DBTrxProvider.close();		
	}
}
