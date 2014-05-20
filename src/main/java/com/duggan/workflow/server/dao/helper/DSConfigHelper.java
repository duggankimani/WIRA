package com.duggan.workflow.server.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.DSConfigDaoImpl;
import com.duggan.workflow.server.dao.model.DataSourceConfig;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.model.form.KeyValuePair;

public class DSConfigHelper {

	public static DSConfiguration save(DSConfiguration configuration){
		DSConfigDaoImpl dao = DB.getDSConfigDao();
		
		DataSourceConfig configModel = get(configuration);
		
		dao.save(configModel);
		
		configuration = get(configModel);
		
		return configuration;
	}

	private static DSConfiguration get(DataSourceConfig model) {
		
		DSConfiguration configuration = new DSConfiguration();
		configuration.setDriver(model.getDriver());
		configuration.setId(model.getId());
		configuration.setJNDI(model.isJNDI());
		configuration.setJNDIName(model.getJNDIName());
		configuration.setName(model.getConfigName());
		configuration.setPassword(model.getPassword());
		configuration.setRDBMS(model.getRDBMS());
		configuration.setStatus(Status.INACTIVE);
		configuration.setURL(model.getURL());
		configuration.setUser(model.getUser());
		configuration.setLastModified(model.getLastModified());
		
		return configuration;
	}

	private static DataSourceConfig get(DSConfiguration configuration) {
		DSConfigDaoImpl dao = DB.getDSConfigDao();
		
		DataSourceConfig model = new DataSourceConfig();
		
		if(configuration.getId()!=null){
			model = dao.getConfiguration(configuration.getId());
		}
		
		model.setId(configuration.getId());
		model.setDriver(configuration.getDriver());
		model.setJNDI(configuration.isJNDI());
		model.setJNDIName(configuration.getJNDIName());
		model.setRDBMS(configuration.getRDBMS());
		model.setPassword(configuration.getPassword());
		model.setUser(configuration.getUser());
		model.setURL(configuration.getURL());
		model.setConfigName(configuration.getName());
		
		return model;
	}
	
	public static List<DSConfiguration> getConfigurations(){
		DSConfigDaoImpl dao = DB.getDSConfigDao();
		
		List<DSConfiguration> configurations  = new ArrayList<>();
		
		List<DataSourceConfig> models = dao.getConfigurations();
		
		for(DataSourceConfig model: models){
			configurations.add(get(model));
		}
		
		return configurations;
	}

	public static boolean delete(Long configurationId) {
		DSConfigDaoImpl dao = DB.getDSConfigDao();
		dao.delete(dao.getConfiguration(configurationId));
		
		return true;
	}
	
	public static List<KeyValuePair> getKeyValuePairs(){
		DSConfigDaoImpl dao = DB.getDSConfigDao();
		List<KeyValuePair> lst = dao.getKeyValuePairs();
		
		return lst;
	}

	public static DSConfiguration getConfigurationByName(String configName) {
		DSConfigDaoImpl dao = DB.getDSConfigDao();
		DSConfiguration config = get(dao.getConfigurationByName(configName));
		
		return config;
	}
}
