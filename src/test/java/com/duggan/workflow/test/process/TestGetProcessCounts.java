package com.duggan.workflow.test.process;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.TaskType;

public class TestGetProcessCounts {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
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
