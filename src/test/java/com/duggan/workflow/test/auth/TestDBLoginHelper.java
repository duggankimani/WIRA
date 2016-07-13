package com.duggan.workflow.test.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.DBLoginHelper;
import com.duggan.workflow.server.helper.auth.callback.DBUserGroupCallbackImpl;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class TestDBLoginHelper {

	DBLoginHelper helper;

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		helper = new DBLoginHelper();
	}
	
	@Test
	public void getGroups(){
		//List<UserGroup> groups = LoginHelper.get().getGroupsForUser("mariano");
	
		//Assert.assertTrue(groups.size()==1);
		
		Assert.assertTrue(new DBUserGroupCallbackImpl().
				getGroupsForUser("mariano", null, null).size()==1);
	}
	
	@Ignore
	public void getAll(){
		HTUser user = new HTUser();
		String email = "mdkimani@gmail.com";
		String userId = "mdkimani";
		String name = "Duggan";
		String password = "kduggan";
		String surname = "Macharia";

		user.setEmail(email);
		user.setUserId(userId);
		user.setName(name);
		user.setPassword(password);
		user.setSurname(surname);
		
		ArrayList<UserGroup> groups= new ArrayList<>();
		UserGroup group = new UserGroup();
		group.setFullName("Finance Department");
		group.setName("FIN");		
		UserGroup g = helper.createGroup(group);
		groups.add(g);
		user.setGroups(groups);
		
		user = helper.createUser(user);
		Assert.assertTrue(user.getGroups().size()==1);

		List<UserGroup> users = helper.getGroupsForUser(userId);
		List<HTUser> groupForUser = helper.getUsersForGroup("FIN");
		
		Assert.assertEquals(users.size(), 1);
		Assert.assertEquals(groupForUser.size(), 1);
	}

	@Ignore
	public void saveUserWithGroup(){
	
		HTUser user = new HTUser();
		String email = "mdkimani@gmail.com";
		String userId = "mdkimani";
		String name = "Duggan";
		String password = "kduggan";
		String surname = "Macharia";

		user.setEmail(email);
		user.setUserId(userId);
		user.setName(name);
		user.setPassword(password);
		user.setSurname(surname);
		
		ArrayList<UserGroup> groups= new ArrayList<>();
		UserGroup group = new UserGroup();
		group.setFullName("Finance Department");
		group.setName("FIN");		
		UserGroup g = helper.createGroup(group);
		groups.add(g);
		user.setGroups(groups);
		
		user = helper.createUser(user);
		Assert.assertTrue(user.getGroups().size()==1);
	}
	
	
	@Ignore
	public void saveGroup(){
		UserGroup group = new UserGroup();
		group.setFullName("Finance Department");
		group.setName("FIN");
		
		UserGroup g = helper.createGroup(group);
		
		Assert.assertNotNull(g.getFullName());
		Assert.assertNotNull(g.getName());
		Assert.assertNotNull(g.getId());
	}
	
	@Ignore
	public void saveUser() {
		HTUser user = new HTUser();
		String email = "mdkimani@gmail.com";
		String userId = "mdkimani";
		String name = "Duggan";
		String password = "kduggan";
		String surname = "Macharia";

		user.setEmail(email);
		user.setUserId(userId);
		user.setName(name);
		user.setPassword(password);
		user.setSurname(surname);

		user = helper.createUser(user);
		Assert.assertNotNull(user.getId());
		Assert.assertNotNull(user.getEmail());
		Assert.assertNotNull(user.getPassword());
		Assert.assertNotNull(user.getSurname());
		Assert.assertNotNull(user.getUserId());
	}

	@After
	public void destroy() throws IOException {
		helper.close();
		DB.rollback();
		DB.closeSession();
	}
}
