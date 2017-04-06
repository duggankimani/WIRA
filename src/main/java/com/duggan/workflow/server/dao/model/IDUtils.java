package com.duggan.workflow.server.dao.model;

import org.apache.commons.lang.RandomStringUtils;

public class IDUtils {

	public static String generateId(){
		//String uuid = UUID.randomUUID().toString();
		//uuid.replaceAll("-", "");
		return RandomStringUtils.random(16, true, true);
	}
	
	public static String generateTempPassword(){
		return RandomStringUtils.random(6,true,true);
	}
}
