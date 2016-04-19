package com.duggan.workflow.test.unassigned;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class GetUnassignedTasks {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void getTasks(){
		int unassigned = DB.getDocumentDao().getUnassigned();
		System.err.println(">> count = "+unassigned);		
		
//		System.err.println(">> Actual - "+DB.getDocumentDao().getUnassignedTasks().size());
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.commitTransaction();
		DB.closeSession();
	}
}
