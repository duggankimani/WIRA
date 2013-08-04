package com.duggan.workflow.test.bpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jbpm.task.identity.LDAPUserGroupCallbackImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Notification;

public class TestNotications {

	@Before
	public void setup(){
		DBTrxProvider.init();
		LoginHelper.get();
	}
	
	@Test
	public void ldap() throws Exception{
		Thread.sleep(1200000);
		List<HTUser> users = LoginHelper.get().getUsersForGroup("HOD_DEV");
		
		Assert.assertTrue(users.size()>0);
	}
	
	@Ignore
	public void setRequest(){
		String approver = "mariano";
		
		DB.beginTransaction();
		Document doc = DocumentDaoHelper.getDocument(50L);
		
		//create approval request
		JBPMHelper.get().createApprovalRequest(doc);
		DB.commitTransaction();
		
		//get tasks for approver
		List<HTSummary> summaries = JBPMHelper.get().getTasksForUser(approver, TaskType.APPROVALREQUESTNEW);
		
		DB.beginTransaction();
		HTSummary summary = summaries.get(0); //last
		Long taskId = summary.getId();
		System.err.println("Id = "+taskId+ ", Size="+summaries.size());
		//claim
		JBPMHelper.get().execute(taskId, approver, Actions.CLAIM, null);
		//start
		JBPMHelper.get().execute(taskId, approver, Actions.START, null);
		
		Map<String, Object> values = new HashMap<>();
		values.put("isApproved", true);
		JBPMHelper.get().execute(taskId, approver, Actions.COMPLETE, values);
		
		DB.commitTransaction();
		DB.closeSession();
	}
	
	@Ignore
	public void getNotifications(){
		DB.beginTransaction();
		List<Notification> notes =  NotificationDaoHelper.getAllNotifications("calcacuervo");
		System.out.println(notes);
		DB.commitTransaction();
		DB.closeSession();
	}
	
	@Ignore
	public void getNotificationCount(){
		DB.beginTransaction();
		Integer count = NotificationDaoHelper.getNotificationCount("calcacuervo");
		System.err.println(count);
		DB.commitTransaction();
		DB.closeSession();
	}

	
	@Ignore
	public void getAlertCount(){
		HashMap<TaskType, Integer> vals = new HashMap<>(); 
		String userId = "calcacuervo";
		JBPMHelper.get().getCount(userId, vals);
		System.err.println(TaskType.APPROVALREQUESTDONE+" : "+vals.get(TaskType.APPROVALREQUESTDONE));
		System.err.println(TaskType.APPROVALREQUESTNEW+" : "+vals.get(TaskType.APPROVALREQUESTNEW));
		
		TaskType type = TaskType.APPROVALREQUESTNEW;
		Integer count = vals.get(type);
		Integer actualCount = 0;		
		List<HTSummary> summary = JBPMHelper.get().getTasksForUser(userId, type);
		actualCount = summary.size();
		Assert.assertEquals(actualCount, count);
		
		TaskType type2 = TaskType.APPROVALREQUESTDONE;
		Integer count2 = vals.get(type2);
		Integer actualCount2 = 0;		
		List<HTSummary> summary2 = JBPMHelper.get().getTasksForUser(userId, type2);
		actualCount2 = summary2.size();
		Assert.assertEquals(actualCount2, count2);
	}
	
	@After
	public void tearDown(){
		try{
			LoginHelper.get().close();
		}catch(Exception e){}
		DBTrxProvider.close();		
	}
}
