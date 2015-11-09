package com.duggan.workflow.test.export;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class TestFormExport {


	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void export(){
		String out = FormDaoHelper.exportForm(1L);
		System.err.println(out);
		//FormDaoHelper.importForm(out+"   ");
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}

}
