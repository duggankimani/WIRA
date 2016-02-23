package com.duggan.workflow.test.bpm;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.ProcessDef;

public class TestSaveProcess {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void getProcessDefsForDocType(){
//		List<ProcessDefModel> models = DB.getProcessDao().getProcessesForDocType(DocType.INVOICE);
//		
//		Assert.assertTrue(models.size()==1);
	}
	
	@Ignore
	public void update(){
		ProcessDef def = ProcessDefHelper.getProcessDef(7L);
		//def.setDocTypes(Arrays.asList(DocType.CONTRACT));
		
		ProcessDefHelper.save(def);
		
		def = ProcessDefHelper.getProcessDef(7L);
		Assert.assertTrue(def.getDocTypes().size()==1);
	}
	
	@Ignore
	public void delete(){
		ProcessDefHelper.delete(14L);
	}
	
	@Ignore
	public void getDocByType(){
		
		ProcessDaoImpl dao = DB.getProcessDao();
	
//		/ProcessDocModel model = dao.getProcessDoc(DocType.LPO);
		
		//Assert.assertNotNull(model);
	}
	
	@Ignore
	public void create(){
		ProcessDef processDef = new ProcessDef();
		//processDef.setDocTypes(Arrays.asList(DocType.CONTRACT, DocType.INVOICE, DocType.LPO));
		processDef.setName("approval-A");
		processDef.setProcessId("invoice-approval");
		processDef = ProcessDefHelper.save(processDef);
		
		Assert.assertNotNull(processDef.getId());
		
		//List<DocType> docTypes = processDef.getDocTypes();
		
//		Assert.assertNotNull(docTypes);
//		
//		Assert.assertTrue(docTypes.size()>0);
		
		List<ProcessDef> processes = ProcessDefHelper.getAllProcesses(true);
		Assert.assertNotNull(processes);
		
		Assert.assertTrue(processes.size()>0);
		
	}
	
	@After
	public void tearDown() {
		DB.commitTransaction();
		DBTrxProviderImpl.close();
	}
}
