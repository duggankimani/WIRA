package com.duggan.workflow.test.process;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;

public class TestTaskClassifications {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
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
		TaskService client = JBPMHelper.get().getTaskClient();
		
		Task task =client.getTaskById(534L);
		
		Long docContentId = task.getTaskData().getDocumentContentId();
		Long outContentId = task.getTaskData().getOutputContentId();
		
		System.out.println("#docContentId "+docContentId+" :: "+" #outContentId = "+outContentId);
//		Delegation delegation = task.getDelegation();
//		List<OrganizationalEntity> entities = delegation.getDelegates();
//		assert entities!=null;
//		assert entities.size()>0;
//		
		//OrganizationalEntity entity = entities.get(0);
//		System.err.println(" >> :: "+entities.size());
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
		
		TaskService client = JBPMHelper.get().getTaskClient();
		List<TaskSummary> summaries=client.getTasksAssignedAsPotentialOwner(userId, language);
		System.out.println("PotentialOwner = "+summaries.size());
		
		summaries=client.getTasksOwned(userId, language);
		System.out.println("Recipient = "+summaries.size());
		
		summaries=client.getTasksOwned(userId, language);
		System.out.println("Tasks Owned = "+summaries.size());
		
		summaries=client.getTasksAssignedAsPotentialOwner(userId, language);
		System.out.println("Task Initiator = "+summaries.size());
		
		summaries=client.getTasksAssignedAsBusinessAdministrator(userId, language);
		System.out.println("Business Administrator = "+summaries.size());
		
		summaries=client.getTasksByStatusByProcessInstanceId(1L, Arrays.asList(Status.Completed),userId);
		System.out.println("Excluded Owner = "+summaries.size());
	}
	
	
	
	@After
	public void destroy(){
		
		DB.rollback();
		DB.closeSession();
	}

}
