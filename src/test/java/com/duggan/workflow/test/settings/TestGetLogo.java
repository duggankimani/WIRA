package com.duggan.workflow.test.settings;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;

public class TestGetLogo {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void get(){
		LocalAttachment attachment = DB.getAttachmentDao().getSettingImage(SETTINGNAME.ORGLOGO);
		Assert.assertNotNull(attachment);
		
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		DB.closeSession();
	}
}
