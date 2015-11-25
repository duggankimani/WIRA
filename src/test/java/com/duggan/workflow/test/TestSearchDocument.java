package com.duggan.workflow.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.jbpm.task.query.TaskSummary;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.model.TaskType;
import com.google.gwt.editor.client.Editor.Ignore;

public class TestSearchDocument {

	DocumentDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		
		DB.beginTransaction();
		dao = DB.getDocumentDao();
	}
	
	@Test
	public void getTasks(){
		String userId = "mariano";
		TaskType type = TaskType.INBOX;
		
		List<HTSummary> summaries = JBPMHelper.get().getTasksForUser(userId, type);
		for(HTSummary summary: summaries){
			System.err.format("%d : %s : %s : %s",
					summary.getId(), summary.getCreated(), summary.getDocumentRef(), 
					summary.getCaseNo())
			.println();
		}
	}
	
	@org.junit.Ignore
	public void searchTasks(){
		String userId ="mariano";
		SearchFilter filter = new SearchFilter();
		filter.setSubject("cnt");
		//filter.setPhrase("DN");
		//filter.setDocType(DocType.LPO);
		//filter.setPriority(Priority.CRITICAL.ordinal());
		//filter.setPriority(Priority.HIGH.ordinal());
		
		//time is an issue
//		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//		Calendar c = Calendar.getInstance();
//		c.set(2013, 7, 27,0,0,0);		
		//filter.setStartDate(c.getTime());		
		
//		c.set(2013, 7, 30,0,0,0);
//		filter.setEndDate(c.getTime());
		
		List<TaskSummary> models = dao.searchTasks(userId,filter);
		
		Assert.assertTrue(models!=null);
		Assert.assertTrue(models.size()>0);
		
		for(TaskSummary m: models){
			System.err.println(m.getSubject()+" : "+m.getCreatedOn()+" : "+m.getProcessInstanceId()+m.getDescription());
		}
		
	}
	
	@Ignore
	public void search(){
		SearchFilter filter = new SearchFilter();
		String userId="calcacuervo";
		//filter.setSubject("900");
		//filter.setPhrase("DN");
		//filter.setDocType(DocType.LPO);
		//filter.setPriority(Priority.CRITICAL.ordinal());
		//filter.setPriority(Priority.HIGH.ordinal());
		
		//time is an issue
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 27,0,0,0);		
		//filter.setStartDate(c.getTime());		
		
		c.set(2013, 7, 30,0,0,0);
		filter.setEndDate(c.getTime());
		
		List<DocumentModel> models = dao.search(userId,filter);
		
		Assert.assertTrue(models!=null);
		Assert.assertTrue(models.size()>0);
		
		for(DocumentModel m: models){
			System.err.println(m);
		}
	}
	
	@After
	public void destroy() throws IOException{
		LoginHelper.get().close();
		DB.closeSession();
	}
}
