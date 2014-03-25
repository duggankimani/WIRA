package com.duggan.workflow.server.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.SettingsDaoImpl;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;

import static com.duggan.workflow.server.dao.helper.FormDaoHelper.*;

public class SettingsDaoHelper {

	public static boolean save(List<Setting> settings){
		
		SettingsDaoImpl dao = DB.getSettingsDao();
		
		for(Setting setting: settings){			
			ADValue advalue = getValue(null, setting.getValue());
			dao.saveSettings(setting.getName(), advalue);
		}
		return false;
	}
	
	public static List<Setting> getSettings(List<SETTINGNAME> names){
		SettingsDaoImpl dao = DB.getSettingsDao();
		List<Setting> settings = new ArrayList<>();
		
		List<ADValue> values = dao.getSettingValues(names);
		for(ADValue advalue: values){
			Value value = getValue(advalue, advalue.getSettingname().getType());
			Setting setting = new Setting(advalue.getSettingname(), value);
			settings.add(setting);
		}
		
		return settings;
	}

}
