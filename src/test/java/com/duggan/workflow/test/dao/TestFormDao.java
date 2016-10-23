package com.duggan.workflow.test.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.Column;
import com.duggan.workflow.shared.model.Schema;

public class TestFormDao {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}

	@Test
	public void getProcessSchema() {
		String processRefId = "rOSplNWnmiCZMws4";
		
		List<Schema> schemas = DB.getFormDao().getProcessSchema(processRefId);
		for(Schema s: schemas){
			System.out.println(">> "+s.getName());
			for(Column col: s.getColumns()){
				System.out.println("   >> "+col.getName());
			}
		}
	}

	@After
	public void tearDown() {
		// DB.rollback();
		DB.commitTransaction();
		DBTrxProviderImpl.close();
	}
}
