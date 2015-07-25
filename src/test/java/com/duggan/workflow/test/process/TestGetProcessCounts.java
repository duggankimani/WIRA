package com.duggan.workflow.test.process;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

public class TestGetProcessCounts {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void getCounts(){
		
		HashMap<TaskType, Integer> counts = new HashMap<>();
		JBPMHelper.get().getProcessCounts("procurementandfinance.ProcurementAndFinanceProcess", counts);
		for(TaskType t: counts.keySet()){
			System.out.println(t.name()+" : "+counts.get(t));
		}
	}
}
