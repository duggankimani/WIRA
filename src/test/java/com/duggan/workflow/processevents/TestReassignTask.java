package com.duggan.workflow.processevents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.transaction.SystemException;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.CaseFilter;
import com.duggan.workflow.shared.model.ProcessLog;
import com.duggan.workflow.shared.model.TaskStepDTO;

public class TestReassignTask {

	@Before
	public void init(){
		DBTrxProviderImpl.init();
//		DB.beginTransaction();
		DB.beginTransaction();
		ProcessMigrationHelper.start(6L);
		DB.commitTransaction();
		DB.closeSession(); //This is required to close the Entity Manager in the current thread so that the next calls receives a new instance
	}	
	
	@Ignore
	public void getTaskStepsForProcess() {
		DB.beginTransaction();
		Long processInstanceId = 111L;
		List<TaskStepDTO> steps = ProcessDaoHelper.getTaskStepsByProcessInstanceId(processInstanceId);
		
		for(TaskStepDTO dto: steps) {
			String desc = dto.getFormName()==null? dto.getOutputDocName() : dto.getFormName();
			System.out.println("Step - "+dto.getStepName()+" :: "+desc);
		}
			
		DB.commitTransaction();
		DB.closeSession();
	}
	
	@Ignore
	public void getProcesses() {
		DB.beginTransaction();
		
		CaseFilter filter = new CaseFilter();
		filter.setProcessId("org.kam.hr.LeaveApplication");
		filter.setCaseNo("Case-0079");
		filter.setUserId("kimani@wira.io");
		List<ProcessLog> logs = DB.getProcessDao().getProcessInstances(filter);
		
		System.out.println("Logs Count = "+logs.size());
		
		DB.commitTransaction();
		DB.closeSession(); //Close session
	}
	
	/**
	 * Reassign worked!!
	 * 
	 * @throws SystemException
	 * @throws NamingException
	 */
	@Test
	public void reassign() throws SystemException, NamingException {
		System.err.println("B4 - Transaction Status = activeTrx = "+DB.getUserTrx().getStatus());
		//6 | Leave Application
		DB.beginTransaction();
		//Task 116		
		Long taskId = 110L;
		String userId= "kimani@wira.io";
		String previousOwner= "tomkim@wira.io";
		JBPMHelper.get().reassignTask(taskId, previousOwner , userId);
		
		System.err.println("On Begin - Transaction Status = activeTrx = "+DB.getUserTrx().getStatus());
		
		long val = ((Long)(System.currentTimeMillis()/1000000));
		DB.getEntityManager().createNativeQuery("insert into test(kaunt) values("+val+")").executeUpdate();
		
		System.err.println("Transaction Status = activeTrx = "+DB.getUserTrx().getStatus());
		DB.commitTransaction();
		DB.closeSession(); //Close session
		
		//New Session
		System.err.println("After - Transaction Status = activeTrx = "+DB.getUserTrx().getStatus());
		DB.beginTransaction();
		Task task = JBPMHelper.get().getSysTask(taskId);
		Status status = task.getTaskData().getStatus();
		System.err.println("Task status = "+status.name());
		
		Long processInstanceId = task.getTaskData().getProcessInstanceId();
		StatefulKnowledgeSession session = (StatefulKnowledgeSession) JBPMHelper.get().getSessionManager().getSession("org.kam.hr.LeaveApplication");
		org.drools.runtime.process.ProcessInstance instance = session.getProcessInstance(processInstanceId);
		System.err.println("Process status = "+instance.getState());
		
		Map<String, Object> values= new HashMap<>();
		JBPMHelper.get().execute(taskId, userId, Actions.START, values);
		DB.commitTransaction();
	}
	
	@Ignore
	public void testTrx() {
		DB.beginTransaction();
		long val = ((Long)(System.currentTimeMillis()/1000000));
		DB.getEntityManager().createNativeQuery("insert into test(kaunt) values("+val+")").executeUpdate();
		DB.commitTransaction();
	}
	
//	@After
	public void destroy(){
		DB.commitTransaction();
//		DB.rollback();
		DB.closeSession();
	}
}
