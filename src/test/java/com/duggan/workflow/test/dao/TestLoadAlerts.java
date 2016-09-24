package com.duggan.workflow.test.dao;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

public class TestLoadAlerts {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
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
