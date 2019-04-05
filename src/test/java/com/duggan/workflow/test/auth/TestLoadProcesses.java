package com.duggan.workflow.test.auth;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class TestLoadProcesses {

	ProcessDaoImpl dao = null;

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		dao = DB.getProcessDao();
	}
	
	@Test
	public void getProcesses(){
		List<ProcessDefModel> list = dao.getAllProcesses();
		System.err.println(list.size());
	}

}
