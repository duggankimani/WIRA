package com.duggan.workflow.test.bpm6.process;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.shared.model.TaskType;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetRequestCountsAction;
import com.duggan.workflow.shared.responses.GetAlertCountResult;
import com.duggan.workflow.shared.responses.GetRequestCountsResult;
import com.duggan.workflow.test.bpm6.AbstractBPM6Test;
import com.duggan.workflow.test.bpm6.StandardDispatchService;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.gwtplatform.dispatch.shared.ActionException;

public class TestGetCounts extends AbstractBPM6Test{

	@Test
	public void getTaskCountsPerTaskName() throws ActionException, ServiceException{
		
		String userId = null;//"kimani@wira.io";
		String processId = "ke.go.kna.KNAEditorialWorkflow";
		
		GetRequestCountsAction action = new GetRequestCountsAction(processId,userId);
		StandardDispatchService dispatcher=  injector.getInstance(StandardDispatchService.class);
		GetRequestCountsResult result = (GetRequestCountsResult) dispatcher.execute(TEST_COOKIE, action);
		
		HashMap<String, Integer> counts = result.getCounts();
		for(String key: counts.keySet()){
			System.err.println(key+" = "+counts.get(key));
		}
	}
	
	
	@Ignore
	public void getUserTaskCounts() throws ActionException, ServiceException{
		String userId = "mdkimani@gmail.com6";
		
		StandardDispatchService dispatcher=  injector.getInstance(StandardDispatchService.class);
		GetAlertCount getCount = new GetAlertCount(userId);
		GetAlertCountResult result = (GetAlertCountResult) dispatcher.execute(TEST_COOKIE, getCount);
		
		HashMap<TaskType, Integer> counts = result.getCounts();
		for(TaskType t: counts.keySet()){
			System.err.println(t+" - "+counts.get(t));
		}
	}

}
