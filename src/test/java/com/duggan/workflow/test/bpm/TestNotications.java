package com.duggan.workflow.test.bpm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.Notification;
import com.wira.commons.shared.models.HTUser;

import junit.framework.Assert;

public class TestNotications {

	@Ignore
	public void setup(){
		DBTrxProviderImpl.init();
	}
	
	@Test
	public void load(){
		HashMap<String, Object> params = new HashMap<>();
		params.put("OwnerId", "Calcacuervo");
		params.put("Description", "This is the description of this description");
		params.put("documentUrl", "#");
		params.put("Subject", "Invoice/ABC/13");
		
		try{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("email.html");			
			String html = "";			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			IOUtils.copy(is, bout);
			html = new String(bout.toByteArray());
			
			html = html.replace("${OwnerId}", params.get("OwnerId").toString());
			html = html.replace("${Description}", params.get("Description").toString());
			html = html.replace("${DocumentURL}", "#");
			html = html.replace("${Subject}", params.get("Subject").toString());
			
			params.put("Body", html);
			
			System.err.println("Body: "+html);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Ignore
	public void getTasksForUser(){
		String userId ="mariano";
		Long processInstanceId = 108L; //88
		//get owned or potential owner of
		List<HTSummary> summaries = JBPMHelper.get().getTasksForUser(null,userId, processInstanceId,0,100);
		
		
		for(HTSummary sum : summaries){
			System.err.format("%d | %s | %s | %d",
					sum.getId(), sum.getCaseNo(),
					sum.getCreated(), sum.getDocumentRef()).println();
		}
		Assert.assertEquals(1, summaries.size());
	}
	
	@Ignore
	public void getcompleted(){
		HashMap<TaskType, Integer> counts = new HashMap<>();
		
		JBPMHelper.get().getCount(null,"james", counts);
		
		Integer count = counts.get(TaskType.COMPLETED);
		
		Assert.assertNotNull(count);
		System.err.println(count);
//		/Assert.assertEquals(new Integer(1), count);
	}
	
	@Ignore
	public void getUsersForGroup(){
		List<HTUser> users = UserDaoHelper.getInstance().getUsersForGroup("HOD_DEV");
		
		Assert.assertTrue(users.size()>0);
		
		Assert.assertEquals("mariano", users.get(0).getUserId());
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
		doc.setCaseNo("INV/20024/2013");
		//doc.setType(DocType.INVOICE);
		doc.setValue("10,000Ksh");
	
		DB.beginTransaction();
		doc = DocumentDaoHelper.save(doc);
		
		//create approval request
		JBPMHelper.get().createApprovalRequest(approver,doc);
		DB.commitTransaction();
		DB.closeSession();
		
		
		DB.beginTransaction();
		//get tasks for approver
		List<HTSummary> summaries = JBPMHelper.get().getTasksForUser(null,approver, TaskType.INBOX,0,100);
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
		HashMap<String, Object> values = new HashMap<>();
		values.put("isApproved", true);
		JBPMHelper.get().execute(taskId, approver, Actions.COMPLETE, values);		
		DB.commitTransaction();
		DB.closeSession();
	}
	
	@Ignore
	public void getNotifications(){
		DB.beginTransaction();
		List<Notification> notes =  NotificationDaoHelper.getAllNotifications(null,"calcacuervo");
		System.out.println(notes);
		DB.commitTransaction();
		DB.closeSession();
	}
	
	@Ignore
	public void getNotificationCount(){
		DB.beginTransaction();
		Integer count = NotificationDaoHelper.getNotificationCount(null,"calcacuervo");
		System.err.println(count);
		DB.commitTransaction();
		DB.closeSession();
	}

	
	@Ignore
	public void getAlertCount(){
		HashMap<TaskType, Integer> vals = new HashMap<>(); 
		String userId = "calcacuervo";
		JBPMHelper.get().getCount(null,userId, vals);
		System.err.println(TaskType.COMPLETED+" : "+vals.get(TaskType.COMPLETED));
		System.err.println(TaskType.INBOX+" : "+vals.get(TaskType.INBOX));
		
		TaskType type = TaskType.INBOX;
		Integer count = vals.get(type);
		Integer actualCount = 0;		
		List<HTSummary> summary = JBPMHelper.get().getTasksForUser(null,userId, type,0,100);
		actualCount = summary.size();
		Assert.assertEquals(actualCount, count);
		
		TaskType type2 = TaskType.COMPLETED;
		Integer count2 = vals.get(type2);
		Integer actualCount2 = 0;		
		List<HTSummary> summary2 = JBPMHelper.get().getTasksForUser(null,userId, type2,0,100);
		actualCount2 = summary2.size();
		Assert.assertEquals(actualCount2, count2);
	}
	
	@Ignore
	public void tearDown(){
		DBTrxProviderImpl.close();		
	}
}
