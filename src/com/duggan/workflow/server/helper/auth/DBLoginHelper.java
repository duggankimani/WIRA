package com.duggan.workflow.server.helper.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.duggan.workflow.server.dao.UserGroupDaoImpl;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;

public class DBLoginHelper implements LoginIntf{

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public boolean login(String username, String password) {
		
		User user = DB.getUserGroupDao().getUser(username);
		
		return user!=null && user.getPassword().equals(password);
	}

	@Override
	public List<HTUser> retrieveUsers() {
		List<HTUser> ht_users = new ArrayList<>();
		
		List<User> users = DB.getUserGroupDao().getAllUsers();
		
		if(users!=null)
		for(User user: users){
			HTUser htuser = get(user);
			ht_users.add(htuser);
		}
		
		return ht_users;
	}

	private HTUser get(User user) {
		HTUser htuser = new HTUser();
		htuser.setEmail(user.getEmail());
		htuser.setUserId(user.getUserId());
		htuser.setName(user.getFirstName());
		htuser.setPassword(user.getPassword());
		htuser.setSurname(user.getLastName());
		htuser.setId(user.getId());
		htuser.setGroups(getFromDb(user.getGroups()));	
		
		return htuser;
	}

	private List<UserGroup> getFromDb(Collection<Group> groups) {
		List<UserGroup> userGroups = new ArrayList<>();
		
		if(groups!=null)
			for(Group group: groups){
				userGroups.add(get(group));
			}
		
		return userGroups;
	}

	@Override
	public HTUser getUser(String userId) {
		User user = DB.getUserGroupDao().getUser(userId);
		
		if(user!=null){
			return get(user);
		}
		
		return null;
	}

	@Override
	public List<UserGroup> retrieveGroups() {
		List<Group> groups = DB.getUserGroupDao().getAllGroups();
		
		List<UserGroup> userGroups = new ArrayList<>();
		if(groups!=null){
			for(Group group: groups){
				UserGroup usergroup = get(group);
				userGroups.add(usergroup);
			}
		}
		
		return userGroups;
	}

	private UserGroup get(Group group) {

		if(group==null){
			return null;
		}
		
		UserGroup userGroup = new UserGroup();
		userGroup.setName(group.getName());
		userGroup.setId(group.getId());
		userGroup.setFullName(group.getFullName());
		
		return userGroup;
	}

	@Override
	public HTUser createUser(HTUser htuser) {
		
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		
		User user = new User();
		
		if(htuser.getId()!=null){
			user = dao.getUser(htuser.getUserId());
		}
		
		user.setId(htuser.getId());
		user.setArchived(false);
		user.setEmail(htuser.getEmail());
		user.setFirstName(htuser.getName());
		user.setLastName(htuser.getSurname());
		user.setPassword(htuser.getPassword());
		user.setUserId(htuser.getUserId());
		user.setGroups(get(htuser.getGroups()));
		
		dao.saveUser(user);
		
		
		return get(user);
	}

	private List<Group> get(List<UserGroup> userGroups) {
		List<Group> groups = new ArrayList<>();
		
		if(userGroups!=null){
			for(UserGroup group: userGroups){
				groups.add(get(group));
			}
		}
		
		return groups;
	}

	private Group get(UserGroup usergroup) {
		
		Group group = new Group();
		group.setFullName(usergroup.getFullName());
		group.setName(usergroup.getName());
		group.setArchived(false);
		group.setId(usergroup.getId());
		
		return group;
	}

	@Override
	public boolean deleteUser(HTUser htuser) {
		
		assert htuser.getId()!=null;
		
		User user = DB.getUserGroupDao().getUser(htuser.getId());
		
		DB.getUserGroupDao().delete(user);
		
		return true;
	}

	@Override
	public List<UserGroup> getGroupsForUser(String userId) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		Collection<Group> groups = dao.getAllGroups(userId);
		
		List<UserGroup> userGroups = new ArrayList<>();
		
		if(groups!=null)
			for(Group group: groups){
				userGroups.add(get(group));
			}
		
		return userGroups;
	}

	@Override
	public List<HTUser> getUsersForGroup(String groupName) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		Collection<User> users = dao.getAllUsers(groupName);
		
		List<HTUser> groupUsers = new ArrayList<>();
		
		if(users!=null)
			for(User user: users){
				groupUsers.add(get(user));
			}
		
		return groupUsers;
	}

	@Override
	public boolean existsUser(String userId) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		User user = dao.getUser(userId);
		
		return user!=null;
	}

	@Override
	public UserGroup createGroup(UserGroup usergroup) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		Group group =  dao.getGroup(usergroup.getName());
		
		if(group==null){
			group = new Group();
		}
		
		group.setFullName(usergroup.getFullName());
		group.setName(usergroup.getName());
		group.setArchived(false);
		
		dao.saveGroup(group);
		
		return get(group);
	}

	@Override
	public boolean deleteGroup(UserGroup userGroup) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		
		Group group = dao.getGroup(userGroup.getName()); 
		
		if(group!=null){
			dao.delete(group);
		}else{
			return false;
		}
		
		return true;
	}

	@Override
	public List<HTUser> getAllUsers() {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		List<User> users = dao.getAllUsers();
		
		List<HTUser> htusers = new ArrayList<>();
		
		if(users!=null)
			for(User user: users){
				htusers.add(get(user));
			}
		
		return htusers;
	}

	@Override
	public List<UserGroup> getAllGroups() {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		
		
		return getFromDb(dao.getAllGroups());
	}
	
}
