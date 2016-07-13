package com.duggan.workflow.test.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.wira.commons.shared.models.PermissionPOJO;

public class TestPermissionDao {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}

	@Test
	public void getProcessRegistry(){
		List<PermissionPOJO> pojos = DB.getPermissionDao().getAllPermissions(0, 100);
		for(PermissionPOJO p: pojos){
			System.err.println("Pojo - "+p.getDescription());
		}
	}
	
	@After
	public void tearDown() {
		//DB.rollback();
		DB.commitTransaction();
		DBTrxProviderImpl.close();
	}

}
