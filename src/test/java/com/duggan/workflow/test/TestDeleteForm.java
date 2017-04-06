package com.duggan.workflow.test;

import org.junit.After;
import org.junit.Before;

import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class TestDeleteForm {

	FormDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		dao = DB.getFormDao();
	}
	
	@After
	public void close(){
		DB.commitTransaction();
		DB.closeSession();
	}

}
