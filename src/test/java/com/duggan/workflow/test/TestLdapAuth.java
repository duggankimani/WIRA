package com.duggan.workflow.test;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class TestLdapAuth {
	
	@Ignore
	public void saveUser(){
		HTUser user = new HTUser();
		user.setEmail("mdkimani@gmail.com");
		user.setUserId("dkimani");
		user.setName("Duggan Kimani");
		user.setPassword("kimani2012");
		user.setSurname("Macharia");
		
		user = LoginHelper.get().createUser(user);
		
		try{
			server();
		}catch(Exception e){}
		
	
	}
	
	@Ignore
	public void getGroupsForUser(){
		List<UserGroup> groups = LoginHelper.get().getGroupsForUser("Administrator");
		
		for(UserGroup group: groups){
			System.err.println(group.getName());
		}
	}
	
	@Ignore
	public void getGroups(){
		List<UserGroup> groups = LoginHelper.get().retrieveGroups();
		
		for(UserGroup group: groups){
			System.err.println(group.getName());
		}
	}
	
	@Ignore
	public void getUsers(){
		List<HTUser> users = LoginHelper.get().retrieveUsers();
		
		for(HTUser user: users){
			System.err.println(user.getUserId()+" : "+user.getName()+" : "+user.getEmail());
		}
		Assert.assertTrue(users.size()>0);
	}
	
	@Ignore
	public void authenticate() throws InterruptedException{
		boolean valid = LoginHelper.get().login("mariano", "pass");

		Assert.assertTrue(valid);
	}
	
	@Ignore
	public void server() throws Exception{
		LoginHelper.get();
		Thread.sleep(3600*1000);
	}
	
	@After
	public void destroy() throws IOException{
		LoginHelper.get().close();
	}
}
