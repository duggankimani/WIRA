package com.duggan.workflow.test.tasksteps;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.MODE;

public class TestTaskSteps {

	ProcessDaoImpl dao = null;

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		dao = DB.getProcessDao();
	}
	
	@Test
	public void create(){
		ADForm form = DB.getFormDao().getForm(1L);
		ADOutputDoc doc = DB.getOutputDocDao().getOuputDocument(1L);
		
		ProcessDefModel processDef = DB.getProcessDao().getProcessDef(1L);
		
		TaskStepModel step = new TaskStepModel();
		step.setCondition("");
		step.setDoc(doc);
		step.setMode(MODE.EDIT);
		step.setStepName("HODApproval");
		processDef.addTaskStep(step);
		dao.createStep(step);
		
		step = new TaskStepModel();
		step.setCondition("");
		step.setForm(form);
		step.setMode(MODE.EDIT);
		step.setStepName("HODApproval");
		processDef.addTaskStep(step);
		dao.createStep(step);		
		
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.commitTransaction();
		DB.closeSession();
	}
}
