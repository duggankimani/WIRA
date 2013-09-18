package com.duggan.workflow.test.bpm;

import java.io.IOException;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.DBUserGroupCallback;

public class TestUserGroupCallback {

	@Before
	public void setup(){
		DBTrxProvider.init();
		//LoginHelper.get();
		DB.beginTransaction();
	}
	
	@Test
	public void create(){
		DBUserGroupCallback callback = new DBUserGroupCallback();
		Group group = new Group();
		String groupId ="HOD_DEV"; 
		group.setName(groupId);
		group.setArchived(false);
		
		User user = new User();
		String userId = "calcacuervo";
		user.setArchived(false);
		user.setUserId(userId);
		user.setFirstName("Calca");
		user.setLastName("Cuervo");
		user.addGroup(group);
		
		callback.saveUser(user);
		Assert.assertNotNull(user.getId());		
		
		group = callback.getGroup(groupId);
		
		Collection<Group> userGroups = user.getGroups();		
		
		Assert.assertTrue(userGroups.size()>0);
		
	}
	
	@After
	public void tearDown() throws IOException{
		DB.rollback();
		//DB.commitTransaction();
		DB.closeSession();
		//LoginHelper.get().close();
	}
	
	
}
