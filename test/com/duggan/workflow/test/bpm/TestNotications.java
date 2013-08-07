package com.duggan.workflow.test.bpm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

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
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Notification;
import com.google.gwt.thirdparty.streamhtmlparser.util.EntityResolver.Status;

public class TestNotications {

	@Before
	public void setup(){
		DBTrxProvider.init();
		LoginHelper.get();
	}
	@Test
	public void getcompleted(){
		HashMap<TaskType, Integer> counts = new HashMap<>();
		
		JBPMHelper.get().getCount("mariano", counts);
		
		Integer count = counts.get(TaskType.APPROVALREQUESTDONE);
		
		Assert.assertNotNull(count);
		Assert.assertEquals(new Integer(1), count);
	}
	
	@Ignore
	public void getUsersForGroup(){
		List<HTUser> users = LoginHelper.get().getUsersForGroup("HOD_DEV");
		
		Assert.assertTrue(users.size()>0);
		
		Assert.assertEquals("mariano", users.get(0).getId());
		Assert.assertEquals("mariano", users.get(0).getName());
	}
	
	@Ignore
	public void setRequest(){
		String approver = "mariano";
		
		Document doc = new Document();
		doc.setCreated(new Date());
		doc.setDateDue(new Date());
		doc.setDescription("Invoice for the purchase of breakfast");
		doc.setDocumentDate(new Date());
		doc.setOwner(new HTUser("calcacuervo"));
		doc.setPartner("Damon Enterprises");
		doc.setPriority(2);
		doc.setStatus(DocStatus.DRAFTED);
		//unique - increment by one on every run
		doc.setSubject("INV/20024/2013");
		doc.setType(DocType.INVOICE);
		doc.setValue("10,000Ksh");
	
		DB.beginTransaction();
		doc = DocumentDaoHelper.save(doc);
		
		//create approval request
		JBPMHelper.get().createApprovalRequest(doc);
		DB.commitTransaction();
		DB.closeSession();
		
		
		DB.beginTransaction();
		//get tasks for approver
		List<HTSummary> summaries = JBPMHelper.get().getTasksForUser(approver, TaskType.APPROVALREQUESTNEW);
		DB.commitTransaction();
		DB.closeSession();
		
		HTSummary summary = summaries.get(0); //last
		Long taskId = summary.getId();
		System.err.println("Id = "+taskId+ ", Size="+summaries.size());
		
		DB.beginTransaction();
		//claim
		JBPMHelper.get().execute(taskId, approver, Actions.CLAIM, null);
		DB.commitTransaction();
		DB.closeSession();
		
		DB.beginTransaction();
		//start
		JBPMHelper.get().execute(taskId, approver, Actions.START, null);
		DB.commitTransaction();
		DB.closeSession();
		
		DB.beginTransaction();
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
