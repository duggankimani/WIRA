package com.duggan.workflow.test.dashboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.DocStatus;

public class TestDashBoardDao {

	DashboardDaoImpl dao;
	
	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();
		dao = DB.getDashboardDao();
	}
	
	@Test
	public void loadMoreValues(){
		//dao.getTaskCompletionData();
		dao.getLongLivingTasks();
	}
	
	@Ignore
	public void getValues(){
		
		dao.getRequestCount(false, DocStatus.DRAFTED);
		dao.getRequestCount(true, DocStatus.INPROGRESS);
		dao.getRequestCount(true, DocStatus.FAILED);
		
		dao.getDocumentCounts();
		dao.getRequestAging();
	}
	
	@After
	public void clearResources(){
		DB.closeSession();
	}
}
