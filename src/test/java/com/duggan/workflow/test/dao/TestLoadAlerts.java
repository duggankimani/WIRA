package com.duggan.workflow.test.dao;

import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTSummary;

public class TestLoadAlerts {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
//		ProcessMigrationHelper.init();
		DB.beginTransaction();
		
	}
	
	@Test
	public void loadProcesses(){
		List<DocumentType> types = DB.getDocumentDao().getDocumentTypes("Administrator");
		for(DocumentType type: types){
			System.out.println(type.getDisplayName()+" - "+type.getInboxCount()+" - "+type.getProcessId());
		}
		Assert.assertTrue(types.size()>0);
	}
	
	@Ignore
	public void loadRecentTasks(){
		String processId="ke.co.workpoint.procurement.PurchaseOrder";
		String userId="kiiru.patrice";
		int limit=100;
		List<Doc> summaries = DocumentDaoHelper.getRecentTasks("rOSplNWnmiCZMws4", userId, 0, limit);
		for(Doc doc:summaries){
			System.err.println("Task - "+doc.getId()+" - "+((HTSummary)doc).getName()+" - "+((HTSummary)doc).getTaskName());
		}
//		List<Doc> summaries= DB.getDocumentDao().getRecentTasks(processId, userId,0, limit);
				
		Assert.assertFalse(summaries.isEmpty());
	}
	
	@Ignore
	public void load(){
		String processRefId=null;//"kGbbZDJP1cbpIoBe";
		HashMap<TaskType, Integer> counts = new HashMap<TaskType, Integer>();
		JBPMHelper.get().getCount(processRefId,"Administrator", counts);
		NotificationDaoHelper.getCounts(processRefId,counts);
	}
	
	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProviderImpl.close();
	}

}
