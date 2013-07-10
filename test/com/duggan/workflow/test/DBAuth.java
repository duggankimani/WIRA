package com.duggan.workflow.test;

public class DBAuth implements Auth{

	@Override
	public boolean authenticate(String username, String password) {
	
		System.err.println("DB authenticate...................");
		
		return true;
	}
}
