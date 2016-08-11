package com.duggan.pg.jsontyp.test;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.form.Form;

public class TestMigrateForms {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
	}
	
	@Test
	public void migrateForms(){
		DB.beginTransaction();

		List<ProcessDef> processes = ProcessDaoHelper.getAllProcesses(null, false);
		
		for(ProcessDef p: processes){
			//Transaction Per Process
			List<Form> adforms = FormDaoHelper.getForms(p.getRefId(),true);
			for(Form form: adforms){
				form.setProcessRefId(p.getRefId());
				Form jsonForm = FormDaoHelper.createJson(form);
				Assert.assertNotNull(jsonForm.getRefId());
			}
		}

		//update taskstepmodels
		DB.getEntityManager().createNativeQuery("update taskstepmodel t set formref=(select refid from adform where id=t.formid) where t.formid is not null and formref is null").executeUpdate();
		DB.commitTransaction();
	}
	
	@After
	public void commit() throws IOException {
		DB.closeSession();
	}
}
