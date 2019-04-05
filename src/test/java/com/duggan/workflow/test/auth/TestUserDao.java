package com.duggan.workflow.test.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.dao.model.UserFilter;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.shared.model.GroupFilter;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class TestUserDao {

	UserDaoHelper helper;

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		helper = new UserDaoHelper();
	}
	
	@Test
	public void getUsers(){
		UserFilter filter = new UserFilter("ad", null, null);
		
		List<User> users = DB.getUserGroupDao().getAllUsers(filter, 0, 10);
		System.err.println(users.size());
	}
	
	@Ignore
	public void getGroups(){
		GroupFilter filter = new GroupFilter(null);
		filter.setUserId("Administrator");
		
		List<Group> groups = DB.getUserGroupDao().getAllGroups(filter, 0, 10);
		System.err.println(groups.size());
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
		
		user = helper.createUser(user,true);
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
		
		user = helper.createUser(user,true);
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

		user = helper.createUser(user,true);
		Assert.assertNotNull(user.getId());
		Assert.assertNotNull(user.getEmail());
		Assert.assertNotNull(user.getPassword());
		Assert.assertNotNull(user.getSurname());
		Assert.assertNotNull(user.getUserId());
	}

	@After
	public void destroy() throws IOException {
		DB.rollback();
		DB.closeSession();
	}
}
