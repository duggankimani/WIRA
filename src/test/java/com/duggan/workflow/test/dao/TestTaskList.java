package com.duggan.workflow.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;

public class TestTaskList {
	
	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}

	@Test
	public void getTaskList(){
		List<Doc> summary = new ArrayList<>();
		String processId = "chasebank.finance.ExpenseClaim";
		
		
		summary.addAll(JBPMHelper.get().getTasksForUser(processId,"Administrator", TaskType.PARTICIPATED,0, 30));

		summary = DocumentDaoHelper.getAllDocumentsJson(processId,0,30,true,false,
				DocStatus.INPROGRESS, DocStatus.REJECTED,DocStatus.APPROVED,
				 DocStatus.COMPLETED);//Current users sent requests
		
		for(Doc doc: summary){
			System.out.println("Values = "+doc.getValues());
		}
	}
	

	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProviderImpl.close();
	}
}
