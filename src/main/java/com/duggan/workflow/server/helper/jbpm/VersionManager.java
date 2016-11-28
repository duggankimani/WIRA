package com.duggan.workflow.server.helper.jbpm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.wira.commons.shared.models.Version;

public class VersionManager {
	
	private Version version;
	private static final String defaultFileName="Version.properties";
	private static VersionManager manager;
	private String fileName="Version.properties";
	
	private VersionManager(String fileName){
		this.fileName = fileName;
		try{
			Properties props = new Properties();
			props.load(getClass().getResourceAsStream("/"+fileName));
			String buildVersion = props.get("version")==null? "x.x.x": props.get("version").toString();
			String timestamp = props.get("timestamp")==null? "":props.get("timestamp").toString() ;
			String buildDate = props.get("build.date")==null? "":props.get("build.date").toString() ;
			
			Date created = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			if(timestamp!=null){
				created = formatter.parse(timestamp);
			}
			
			version = new Version(buildVersion, created, buildDate);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static VersionManager get(String aFileName){
		if(manager==null || !manager.fileName.equals(aFileName)){
			manager = new VersionManager(aFileName);
		}
		
		return manager;
	}
	
	private static VersionManager get(){
		return get(defaultFileName);
	}
	
	public static Version getVersion(){
		return get().version;
	}
	
	public static Version getVersion(String versionFileName){
		return get(versionFileName).version;
	}
	
}
