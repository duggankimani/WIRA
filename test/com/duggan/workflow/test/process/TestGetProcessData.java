package com.duggan.workflow.test.process;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.HTSummary;

public class TestGetProcessData {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		//ProcessMigrationHelper.init();
	}
	
	@Test
	public void getParameters(){
		Long taskId = 432L;
		HTSummary summary = JBPMHelper.get().getSummary(taskId);
		Assert.assertNotNull(summary.getDetails());
		Assert.assertTrue(summary.getDetails().size()>0);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
