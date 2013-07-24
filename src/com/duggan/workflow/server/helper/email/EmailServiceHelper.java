package com.duggan.workflow.server.helper.email;

import java.util.Properties;

public class EmailServiceHelper {

	static Properties props = new Properties();
	
	static{		
		try{
			props.load(ClassLoader.getSystemResourceAsStream("smtp.properties"));			
		}catch(Exception e){};
		
	}
	
	public static String getProperty(String name){
		String val = props.getProperty(name);
		return val;
	}
	
	public static void sendEmail(String from, String to, String subject, String message){
		
	}
}
