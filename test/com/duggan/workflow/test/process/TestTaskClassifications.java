package com.duggan.workflow.test.process;

import java.util.List;

import org.jbpm.task.Delegation;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
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
		ProcessMigrationHelper.start(1L);
//		ProcessMigrationHelper.start(4L);
	}
	
	@Test
	public void getActors(){
		long processInstanceId = 508L;
		JBPMHelper.get().getWorkflowProcessDia(processInstanceId);
	}
	
	@Ignore
	public void getDelegatedTask(){
		
		String userId="mariano";
		String language="en-UK";
		LocalTaskService client = JBPMHelper.get().getTaskClient();
		
		Task task =client.getTask(534L);
		
		Long docContentId = task.getTaskData().getDocumentContentId();
		Long outContentId = task.getTaskData().getOutputContentId();
		
		System.out.println("#docContentId "+docContentId+" :: "+" #outContentId = "+outContentId);
		Delegation delegation = task.getDelegation();
		List<OrganizationalEntity> entities = delegation.getDelegates();
		assert entities!=null;
		assert entities.size()>0;
		
		//OrganizationalEntity entity = entities.get(0);
		System.err.println(" >> :: "+entities.size());
//		Map<String,Object> data = JBPMHelper.getMappedDataByContentId(outContentId);
//		for(String key: data.keySet()){
//			System.err.println(key+" - "+data.get(key));
//		}
	}
	
	@Ignore
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
