package com.duggan.workflow.test;

public class Authenticate {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		//String classname = "com.duggan.workflow.test.DBAuth";
		String classname = "com.duggan.workflow.test.LDAPAuth";
		
		Class<?> authclass = Class.forName(classname);
		
		Auth auth =(Auth)authclass.newInstance();
		
		auth.authenticate("", "");
		// 
	}
}
