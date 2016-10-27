package com.duggan.workflow.test;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.HTask;

public class TestTaskData {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
	}
	
	@Test
	public void getData(){
		Long taskId = 332L;
		HTask task = JBPMHelper.get().getTask(taskId);
		for(String key: task.getValues().keySet()){
			System.err.println("{"+key+":"+task.get(key)+"}");
		}
		
		Map<String, Object> taskVals= JBPMHelper.getMappedData(JBPMHelper.get().getSysTask(taskId));
		for(String key: taskVals.keySet()){
			Object val = taskVals.get(key);
			System.err.println("{"+key+":"+val+"}");
			
			if(val instanceof Doc){
				System.err.println("==============DOC===============");
				Doc doc = (Doc)val;
				for(String key1: task.getValues().keySet()){
					System.err.println("{"+key1+":"+doc.get(key1)+"}");
				}
				System.err.println(doc.getDetails()+"");
			}
		}
		
	}
	
	@org.junit.After
	public void destroy(){
		DB.closeSession();
	}
}
