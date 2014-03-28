package com.duggan.workflow.server.dao.helper;

import java.util.ArrayList;
import java.util.Arrays;
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
			Value value = getValue(advalue, advalue.getSettingName().getType());
			Setting setting = new Setting(advalue.getSettingName(), value);
			settings.add(setting);
		}
		
		return settings;
	}

	public static Setting getSetting(SETTINGNAME settingName) {
		List<Setting> settings = getSettings(Arrays.asList(settingName));
		
		if(settings==null || settings.isEmpty()){
			return null;
		}
		
		return settings.get(0);
	}

	public static Object getSettingValue(SETTINGNAME settingName) {
		
		Setting setting = getSetting(settingName);
		return setting==null? null: setting.getValue().getValue();
	}

}
