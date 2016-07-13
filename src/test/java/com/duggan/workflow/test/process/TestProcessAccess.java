package com.duggan.workflow.test.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.Listable;

public class TestProcessAccess {

	long processDefId = 1L;

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		//ProcessMigrationHelper.start(processDefId);
	}

	@Test
	public void create() {
		ProcessDef processDef = ProcessDaoHelper.getProcessDef(processDefId);
		ArrayList<Listable> usersAndGroups = new ArrayList<>();
		usersAndGroups.add(new HTUser("Administrator"));
		processDef.setUsersAndGroups(usersAndGroups);
		
		ProcessDef processDef1 = ProcessDaoHelper.save(processDef);
		System.err.println("Accesses >> "+processDef1.getUsersAndGroups().size());
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.commitTransaction();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
