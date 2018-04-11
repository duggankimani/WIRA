package com.duggan.workflow.server.helper.auth;

import java.io.Closeable;
import java.util.List;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public interface LoginIntf extends Closeable {

	 /* @param username
	 * @param password
	 * @return true if a user with the given password & username is found
	 */
	public boolean login(String username, String password);
	
	/**
	 * Retrieve All Users
	 * 
	 * @return
	 */
	public List<HTUser> retrieveUsers();
	
	public HTUser getUser(String userId);
	

	/**
	 * 
	 * @return All groups
	 */
	public List<UserGroup> retrieveGroups();
	
	/**
	 * Create/Update New User
	 */
	public HTUser createUser(HTUser user, boolean isSendActivationEmail);
	

	public boolean deleteUser(HTUser user);
	
	public UserGroup createGroup(UserGroup group);
	
	public boolean deleteGroup(UserGroup group);
	
	/**
	 * Note The original ldif maps the users using the common name : e.g
	 * uniqueMember: cn=salaboy,ou=users,o=mojo; therefore any attempts to
	 * retrieve groups for user using the uid i.e uniqueMember:
	 * uid=salaboy,ou=users,o=mojo; fails!!! - see EqualsFilter below
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserGroup> getGroupsForUser(String userId);
	
	public List<HTUser> getUsersForGroup(String groupName);
	
	public boolean existsUser(String userId);

	public List<HTUser> getAllUsers(String searchTerm, Integer offset, Integer limit);

	public List<UserGroup> getAllGroups(String searchTerm, Integer offset, Integer limit);

	public UserGroup getGroupById(String groupId);

	HTUser getUser(String userId, boolean loadGroups);

	public List<HTUser> getUsersForGroups(String[] groups);

	public boolean updatePassword(String username, String password);

	public Integer getUserCount(String searchTerm);
	
}
