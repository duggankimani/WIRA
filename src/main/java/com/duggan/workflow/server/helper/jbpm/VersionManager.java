package com.duggan.workflow.server.helper.jbpm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.duggan.workflow.shared.model.Version;

public class VersionManager {
	
	private Version version;
	private static VersionManager manager;
	
	private VersionManager(){
		try{
			Properties props = new Properties();
			props.load(getClass().getResourceAsStream("/Version.properties"));
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
	
	private static VersionManager get(){
		if(manager==null){
			manager = new VersionManager();
		}
		
		return manager;
	}
	public static Version getVersion(){
		return get().version;
	}
	
}
