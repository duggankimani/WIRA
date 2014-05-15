package com.duggan.workflow.test.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.form.KeyValuePair;

public class TestDSDao {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void execute(){
		List<KeyValuePair> values = DB.getDSConfigDao().getKeyValuePairs();
		
		System.err.println(values.size());
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}
}
