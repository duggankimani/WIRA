package com.duggan.workflow.test.bpm;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.model.ProcessDocModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.dao.ProcessDefHelper;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.ProcessDef;

public class TestSaveProcess {

	@Before
	public void setup() {
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void create(){
		ProcessDef processDef = new ProcessDef();
		processDef.setDocTypes(Arrays.asList(DocType.CONTRACT, DocType.INVOICE));
		processDef.setName("approval-A");
		processDef.setProcessId("invoice-approval");
		processDef = ProcessDefHelper.save(processDef);
		
		Assert.assertNotNull(processDef.getId());
		
		List<DocType> docTypes = processDef.getDocTypes();
		
		Assert.assertNotNull(docTypes);
		
		Assert.assertTrue(docTypes.size()>0);
		
		List<ProcessDef> processes = ProcessDefHelper.getAllProcesses();
		Assert.assertNotNull(processes);
		
		Assert.assertTrue(processes.size()>0);
		
	}
	
	@After
	public void tearDown() {
		DB.commitTransaction();
		DBTrxProvider.close();
	}
}
