package com.duggan.workflow.server.helper.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

/**
 * This is a helper class for the authentication process
 * 
 * @author duggan
 * 
 */
public class LoginHelper implements LoginIntf{

	private static LoginHelper helper;

	LoginIntf impl;

	private LoginHelper() {
		//impl = new LDAPLoginHelper();
		impl = new UserDaoHelper();
	}

	/**
	 * Thread safe Singleton
	 * 
	 * @return LoginHelper
	 */
	public static LoginHelper get() {
		if (helper == null) {
			synchronized (LoginHelper.class) {
				if (helper == null) {
					helper = new LoginHelper();
				}
			}

		}

		return helper;
	}
	
	
	

	public static LoginHelper getHelper() {
		return get();
	}

	@Override
	public void close() throws IOException {
		impl.close();
	}

	@Override
	public boolean login(String username, String password) {
		return impl.login(username, password);
	}

	@Override
	public List<HTUser> retrieveUsers() {
		
		return impl.retrieveUsers();
	}

	@Override
	public HTUser getUser(String userId){
		return getUser(userId, false);
	}
	
	@Override
	public HTUser getUser(String userId, boolean loadGroups) {
		return impl.getUser(userId,loadGroups);
	}

	@Override
	public List<UserGroup> retrieveGroups() {
		return impl.retrieveGroups();
	}

	public HTUser createUser(HTUser user) {
		return createUser(user, false);
	}
	
	public HTUser createUser(HTUser user, boolean isSendActivationEmail) {
		return impl.createUser(user,isSendActivationEmail);
	}

	@Override
	public boolean deleteUser(HTUser user) {
		return impl.deleteUser(user);
	}

	@Override
	public List<UserGroup> getGroupsForUser(String userId) {
		return impl.getGroupsForUser(userId);
	}

	@Override
	public List<HTUser> getUsersForGroup(String groupName) {
		return impl.getUsersForGroup(groupName);
	}

	public boolean existsUser(String userId) {
		return impl.existsUser(userId);
	}

	@Override
	public UserGroup createGroup(UserGroup group) {
		return impl.createGroup(group);
	}

	@Override
	public boolean deleteGroup(UserGroup group) {
		return impl.deleteGroup(group);
	}

	public List<HTUser> getAllUsers(String searchTerm, Integer offset, Integer limit) {

		return impl.getAllUsers(searchTerm, offset, limit);
	}
	

	public Integer getUserCount(String searchTerm) {
		
		return impl.getUserCount(searchTerm);
	}

	public List<UserGroup> getAllGroups(String searchTerm, Integer offset, Integer limit) {
		
		return impl.getAllGroups(searchTerm, offset, limit);
	}

	@Override
	public UserGroup getGroupById(String groupId) {
		return impl.getGroupById(groupId);
	}

	public List<HTUser> getUsersForGroups(String[] groups) {

		return impl.getUsersForGroups(groups);
	}

	public List<UserGroup> getGroupsByIds(String[] groups) {
		if(groups==null || groups.length==0){
			return new ArrayList<>();
		}
		
		List<UserGroup> groupList = new ArrayList<>();
		for(String groupId: groups){
			UserGroup group = impl.getGroupById(groupId);
			if(group!=null){
				groupList.add(group);
			}
		}
		
		return groupList;
	}

	public boolean updatePassword(String username, String password) {
		return impl.updatePassword(username, password);
	}

}
