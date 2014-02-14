package com.duggan.workflow.test.export;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.FormDaoHelper;

public class TestFormExport {


	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void export(){
		String out = FormDaoHelper.exportForm(4L);
		System.err.println(out);
		FormDaoHelper.importForm(out+"   ");
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}

}
