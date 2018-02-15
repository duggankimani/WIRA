package com.duggan.workflow.test.leave;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.SearchFilter;

public class TestLeaveSearch {


	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(6L);
	}
	
	@Test
	public void search() {
		SearchFilter filter = new SearchFilter();
		filter.setPhrase("41");
		List<Document> docs = DocumentDaoHelper.searchJson("org.kam.hr.LeaveApplication", "kimani@wira.io", filter);
		for(Document doc: docs) {
			System.err.println(">> doc -> "+doc.getCaseNo()+", created:"+doc.getCreated());
		}
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
