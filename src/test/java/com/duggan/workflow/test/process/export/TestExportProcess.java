package com.duggan.workflow.test.process.export;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class TestExportProcess {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void export(){
		String processRefId="4uSQJcExcNyBGYED";
//		ProcessDaoHelper.exportProcess(processRefId);
	}
	
	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
	}

}
