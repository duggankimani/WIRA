package com.duggan.workflow.test.settings;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;

public class TestSettingsDao {

	
	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void get(){
		List<Setting> settings = SettingsDaoHelper.getSettings(Arrays.asList(SETTINGNAME.ORGNAME));
		
		Assert.assertNotNull(settings);
		Assert.assertEquals(1, settings.size());
		
		Setting setting = settings.get(0);
		System.err.println("OrgName = "+setting.getValue().getValue());
		
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		DB.closeSession();
	}

}
