package com.duggan.workflow.processevents;

import java.util.HashMap;
import java.util.HashMap;

import org.jbpm.task.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.StringValue;

public class TestWiraProcessEventListener {

	@Before
	public void init(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(2L);
	}
	
	
	@Test
	public void testComplete(){
		//Tests - Case#19, 14, 05, 12, 11
		//pid 68,63,42,40,29
		Long processInstanceId = 42L;
		
		Number id = (Number) DB.getEntityManager().createNativeQuery("Select id from task where processinstanceid=:pid and status!=:status")
		.setParameter("pid", processInstanceId)
		.setParameter("status", Status.Completed.name()).getSingleResult();
		
		Long taskId = id.longValue();
		
		HashMap<String,Object> values = new HashMap<>();
		values.put("isHodApproved", "Denied");
		
		HTask task = JBPMHelper.get().getTask(taskId);
		task.setValue("isHodApproved", new StringValue("isHodApproved"));
		values.put("documentOut", task);
		//JBPMHelper.get().execute(taskId, "kiiru.patrice", Actions.START, values);
		JBPMHelper.get().execute(taskId, "kiiru.patrice", Actions.COMPLETE, values);
		
	}
	
	@After
	public void destroy(){
		//DB.commitTransaction();
		DB.rollback();
		DB.closeSession();
	}
}
