package com.duggan.workflow.test.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.task.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.TaskLog;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.ProcessMappings;

public class TestGetProcessData {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(6L);
//		ProcessMigrationHelper.start(14L);
//		ProcessMigrationHelper.start(16L);
//		ProcessMigrationHelper.start(4L);
//		ProcessMigrationHelper.start(1L);
	}
	
	@Ignore
	public void getTaskLog(){
		List<TaskLog> logs = DB.getProcessDao().getProcessLog(55L, "en-UK");
		for(TaskLog log: logs){
			System.err.println(log);
		}
	}
	
	@Ignore
	public void getTaskNodes(){
		List<TaskNode> nodes = JBPMHelper.get().getWorkflowProcessNodes("invoice-approval");
		
		for(TaskNode node: nodes){
			System.err.println(node.getDisplayName());
		}
	}
	
	
	@Ignore
	public void submitTask(){
		String userId="calcacuervo";
		Long taskId=2458L;
		Actions action = Actions.COMPLETE;
		
		HashMap<String, Object> vals = new HashMap<>();
		
		HashMap<String, Value> values = new HashMap<String, Value>();
		if(values!=null){
			long processInstanceId=0L;
			Task task = JBPMHelper.get().getTaskClient().getTask(taskId);
			processInstanceId = task.getTaskData().getProcessInstanceId();
						
			Document document = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId,false);
			assert document!=null;
			
			ProcessMappings mappings = JBPMHelper.get().getProcessDataMappings(taskId);
			
			for(String key: values.keySet()){
				Value value = values.get(key);				
				vals.put(key, value==null?null: value.getValue());
				if(key!=null){
					key = mappings.getOutputName(key);
					document.setValue(key,value);
				}
			}
			vals.put("documentOut", document);
		}
		
		JBPMHelper.get().execute(taskId, userId, action, vals);
	}
	
	@Ignore
	public void getTask(){
		Long taskId = 2456L;
		HTSummary task = JBPMHelper.get().getTask(taskId);
		
	}
	
	@Ignore
	public void getTaskParameters(){
		Long taskId = 1506L;
		String name= JBPMHelper.get().getDisplayName(taskId);
		
		System.out.println("Name ="+name);
	}
	
	@Ignore
	public void getParameterz(){
		//Long taskId = 1518L;
		Long taskId = 1176L;
		Task task = JBPMHelper.get().getSysTask(taskId);
		
		Map<String,Object> vls = JBPMHelper.get().getMappedData(task);
		for(String key: vls.keySet()){
			Object v = vls.get(key);
			System.err.println(key+" - "+v);
		}
		
		Assert.assertNotNull(vls);
		Assert.assertNotSame(vls.size(), 0);
		
		//Assert.assertNotNull(summary.getDetails());
		//Assert.assertTrue(summary.getDetails().size()>0);
	}
	
	@Test
	public void getParameters(){
		//Long taskId = 1518L;
		//Long taskId = 1176L;
		Long taskId = 1174L;
		HTask summary = JBPMHelper.get().getTask(taskId);
		System.err.println(summary);
		HashMap<String,Value> vls = summary.getValues();
		for(String key: vls.keySet()){
			Value v = vls.get(key);
			
			System.err.println(key+" - "+(v==null? null : v.getValue()));
		}
		
		for(String key: summary.getDetails().keySet()){
			System.err.println(key + " =  "+summary.getDetails().get(key));
		}
		
		HashMap<String, Value> values = summary.getValues();
		Assert.assertNotNull(values);
		Assert.assertNotSame(values.size(), 0);
		
		//Assert.assertNotNull(summary.getDetails());
		//Assert.assertTrue(summary.getDetails().size()>0);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
