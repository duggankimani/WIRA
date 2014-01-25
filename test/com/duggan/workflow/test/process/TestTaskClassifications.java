package com.duggan.workflow.test.process;

import java.util.List;
import java.util.Map;

import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.local.LocalTaskService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;

public class TestTaskClassifications {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
//		ProcessMigrationHelper.start(1L);
//		ProcessMigrationHelper.start(4L);
	}
	
	@Ignore
	public void getDelegatedTask(){
		
		String userId="calcacuervo";
		String language="en-UK";
		LocalTaskService client = JBPMHelper.get().getTaskClient();
		
		Task task =client.getTask(423L);
		
		Long docContentId = task.getTaskData().getDocumentContentId();
		Long outContentId = task.getTaskData().getOutputContentId();
		
		System.out.println("#docContentId "+docContentId+" :: "+" #outContentId = "+docContentId);
		Map<String,Object> data = JBPMHelper.getMappedDataByContentId(outContentId);
		for(String key: data.keySet()){
			System.err.println(key+" - "+data.get(key));
		}
	}
	
	@Test
	public void getTaskCategories(){
		String userId="gatheru";
		String language="en-UK";
		Status status = Status.Created;
		
		LocalTaskService client = JBPMHelper.get().getTaskClient();
		List<TaskSummary> summaries=client.getTasksAssignedAsPotentialOwner(userId, language);
		System.out.println("PotentialOwner = "+summaries.size());
		
		summaries=client.getTasksAssignedAsRecipient(userId, language);
		System.out.println("Recipient = "+summaries.size());
		
		summaries=client.getTasksOwned(userId, language);
		System.out.println("Tasks Owned = "+summaries.size());
		
		summaries=client.getTasksAssignedAsTaskInitiator(userId, language);
		System.out.println("Task Initiator = "+summaries.size());
		
		summaries=client.getTasksAssignedAsTaskStakeholder(userId, language);
		System.out.println("Stake Holder = "+summaries.size());
		
		summaries=client.getTasksAssignedAsExcludedOwner(userId, language);
		System.out.println("Excluded Owner = "+summaries.size());
	}
	
	
	
	@After
	public void destroy(){
		
		DB.rollback();
		DB.closeSession();
	}

}
