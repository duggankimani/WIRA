package com.duggan.workflow.server.helper.auth;

import java.io.IOException;
import java.util.List;

import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;

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
		impl = new LDAPLoginHelper();
		//impl = new DBLoginHelper();
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
	public HTUser getUser(String userId) {
		return impl.getUser(userId);
	}

	@Override
	public List<UserGroup> retrieveGroups() {
		return impl.retrieveGroups();
	}

	@Override
	public HTUser createUser(HTUser user) {
		return impl.createUser(user);
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
}
