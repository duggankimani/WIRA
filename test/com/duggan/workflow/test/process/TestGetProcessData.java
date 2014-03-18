package com.duggan.workflow.test.process;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jbpm.task.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.Value;

public class TestGetProcessData {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(14L);
	}
	
	@Ignore
	public void getTaskParameters(){
		Long taskId = 1506L;
		String name= JBPMHelper.get().getDisplayName(taskId);
		
		System.out.println("Name ="+name);
	}
	
	@Test
	public void getParameterz(){
		Long taskId = 1518L;
		Task task = JBPMHelper.get().getSysTask(taskId);
		
		Map<String,Object> vls = JBPMHelper.get().getMappedData(task);
		for(String key: vls.keySet()){
			Object v = vls.get(key);
			System.err.println(key+" - "+v);
		}
		
		Assert.assertNotNull(vls);
		Assert.assertNotSame(vls.size(), 0);
		
		//Assert.assertNotNull(summary.getDetails());
		//Assert.assertTrue(summary.getDetails().size()>0);
	}
	
	@Ignore
	public void getParameters(){
		Long taskId = 1518L;
		HTask summary = JBPMHelper.get().getTask(taskId);
		Map<String,Value> vls = summary.getValues();
		for(String key: vls.keySet()){
			Value v = vls.get(key);
			System.err.println(key+" - "+v.getValue());
		}
		Map<String, Value> values = summary.getValues();
		Assert.assertNotNull(values);
		Assert.assertNotSame(values.size(), 0);
		
		//Assert.assertNotNull(summary.getDetails());
		//Assert.assertTrue(summary.getDetails().size()>0);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
