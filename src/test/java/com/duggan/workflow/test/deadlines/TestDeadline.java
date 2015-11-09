package com.duggan.workflow.test.deadlines;

import java.util.List;

import org.jbpm.task.Deadline;
import org.jbpm.task.Deadlines;
import org.jbpm.task.Task;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

public class TestDeadline {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
//		ProcessMigrationHelper.start(1L);
//		ProcessMigrationHelper.start(4L);
	}
	
	@Test
	public void getDealines(){
		long taskId=1953L;
		
		Task task = JBPMHelper.get().getSysTask(taskId);
		Deadlines deadlines = task.getDeadlines();
		List<Deadline> startDeadLines = deadlines.getStartDeadlines();
		List<Deadline> endDeadLines = deadlines.getEndDeadlines();
		
		Assert.assertNotNull(startDeadLines);
		Assert.assertNotNull(endDeadLines);
		System.err.println("Yeah!! "+startDeadLines.size()+" - "+endDeadLines.size());
		
		for(Deadline deadline: startDeadLines){
			System.err.println("Start Deadline: "+deadline.getDate());
		}

		for(Deadline deadline: endDeadLines){
			System.err.println("End Deadline: "+deadline.getDate());
		}

	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}
}
