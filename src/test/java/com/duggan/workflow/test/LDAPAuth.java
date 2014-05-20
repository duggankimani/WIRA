package com.duggan.workflow.test;

public class LDAPAuth implements Auth{
	
	@Override
	public boolean authenticate(String username, String password) {
	
		System.err.println("LDAP Authenticate....................");
		
		return true;
	}
}
