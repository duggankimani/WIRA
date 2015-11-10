package com.duggan.workflow.server.db;

import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationSettings {

	Properties properties = new Properties();
	private static Logger log = Logger.getLogger(ApplicationSettings.class);
	
	public ApplicationSettings() {
		try {
			properties.load(ApplicationSettings.class.getClassLoader()
					.getResourceAsStream("general.properties"));
		}catch(Exception e){
			log.error("Load Application Settings Failed cause: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static ApplicationSettings settings;
	public static ApplicationSettings getInstance(){
		if(settings==null){
			synchronized (ApplicationSettings.class) {
				if(settings==null){
					settings = new ApplicationSettings();
				}
			}
		}
		
		return settings;
	}
	
	public String getProperty(String key){
		return properties.getProperty(key);
	}
}
