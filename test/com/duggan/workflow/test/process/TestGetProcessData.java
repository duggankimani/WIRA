package com.duggan.workflow.test.process;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.Value;

public class TestGetProcessData {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		//ProcessMigrationHelper.init();
	}
	
	@Test
	public void getParameters(){
		Long taskId = 461L;
		HTSummary summary = JBPMHelper.get().getSummary(taskId);
		
		Map<String,Value> vls = summary.getValues();
		for(String key: vls.keySet()){
			Value v = vls.get(key);
			System.err.println(key+" - "+v.getValue());
		}
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
