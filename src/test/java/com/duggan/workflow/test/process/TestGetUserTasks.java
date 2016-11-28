package com.duggan.workflow.test.process;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.TaskType;

public class TestGetUserTasks{

	

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(2L);
	}
	
	@Test
	public void getInboxTasks(){
//		String userId = "mutira.george";
		String userId = "wangila.arnold";
		
		List<HTSummary> summaries = JBPMHelper.get().getTasksForUser(null,userId, TaskType.INBOX, 0, 100);
		System.err.println("Summaries Count: "+summaries.size());
	}
}
