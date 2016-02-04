package com.duggan.workflow.server.helper.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.duggan.workflow.server.dao.UserGroupDaoImpl;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.Org;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Organization;
import com.duggan.workflow.shared.model.UserGroup;

public class DBLoginHelper implements LoginIntf {

	@Override
	public void close() throws IOException {

	}

	@Override
	public boolean login(String username, String password) {

		User user = DB.getUserGroupDao().getUser(username);

		return user != null && user.getPassword().equals(password);
	}

	@Override
	public List<HTUser> retrieveUsers() {
		List<HTUser> ht_users = new ArrayList<>();

		List<User> users = DB.getUserGroupDao().getAllUsers();

		if (users != null)
			for (User user : users) {
				HTUser htuser = get(user);
				ht_users.add(htuser);
			}

		return ht_users;
	}

	private HTUser get(User user) {
		return get(user, false);
	}

	public HTUser get(User user, boolean loadGroups) {
		HTUser htuser = new HTUser();
		htuser.setEmail(user.getEmail());
		htuser.setUserId(user.getUserId());
		htuser.setName(user.getFirstName());
		htuser.setPassword(user.getPassword());
		htuser.setSurname(user.getLastName());
		htuser.setId(user.getId());
		htuser.setOrganization(getOrg(user.getOrganization()));

		if (loadGroups) {
			htuser.setGroups(getFromDb(user.getGroups()));
			JBPMHelper.get().setCounts(htuser);
		}

		return htuser;
	}

	private List<UserGroup> getFromDb(Collection<Group> groups) {
		List<UserGroup> userGroups = new ArrayList<>();

		if (groups != null)
			for (Group group : groups) {
				userGroups.add(get(group));
			}

		return userGroups;
	}

	@Override
	public HTUser getUser(String userId) {
		return getUser(userId, false);
	}

	@Override
	public HTUser getUser(String userId, boolean loadGroups) {
		User user = DB.getUserGroupDao().getUser(userId);

		if (user != null) {
			return get(user, loadGroups);
		}

		return null;
	}

	@Override
	public List<UserGroup> retrieveGroups() {
		List<Group> groups = DB.getUserGroupDao().getAllGroups();

		List<UserGroup> userGroups = new ArrayList<>();
		if (groups != null) {
			for (Group group : groups) {
				UserGroup usergroup = get(group);
				userGroups.add(usergroup);
			}
		}

		return userGroups;
	}

	public UserGroup get(Group group) {

		if (group == null) {
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

		if (htuser.getId() != null) {
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

		Org org = dao.getOrganizationByName(htuser.getOrganization().getName());

		user.setOrganization(org);

		dao.saveUser(user);

		return get(user);
	}

	private Collection<Org> getOrgs(List<Organization> organizations) {
		Collection<Org> orgs = new ArrayList<>();
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		for (Organization organization : organizations) {
			Org org = dao.getOrganizationByName(organization.getName());
			if (org != null) {
				orgs.add(org);
			}
		}
		return orgs;
	}

	private List<Group> get(List<UserGroup> userGroups) {
		List<Group> groups = new ArrayList<>();

		if (userGroups != null) {
			for (UserGroup group : userGroups) {
				groups.add(get(group));
			}
		}

		return groups;
	}

	private Group get(UserGroup usergroup) {

		Group group = new Group();
		if (usergroup.getId() != null) {
			UserGroupDaoImpl dao = DB.getUserGroupDao();
			group = dao.getGroup(usergroup.getName());
		}
		group.setFullName(usergroup.getFullName());
		group.setName(usergroup.getName());
		group.setArchived(false);

		return group;
	}

	@Override
	public boolean deleteUser(HTUser htuser) {

		assert htuser.getId() != null;

		User user = DB.getUserGroupDao().getUser(htuser.getId());

		DB.getUserGroupDao().delete(user);

		return true;
	}

	@Override
	public List<UserGroup> getGroupsForUser(String userId) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		Collection<Group> groups = dao.getAllGroups(userId);

		List<UserGroup> userGroups = new ArrayList<>();

		if (groups != null)
			for (Group group : groups) {
				userGroups.add(get(group));
			}

		return userGroups;
	}

	@Override
	public List<HTUser> getUsersForGroup(String groupName) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		Collection<User> users = dao.getAllUsers(groupName);

		List<HTUser> groupUsers = new ArrayList<>();

		if (users != null)
			for (User user : users) {
				groupUsers.add(get(user));
			}

		return groupUsers;
	}

	@Override
	public boolean existsUser(String userId) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		User user = dao.getUser(userId);

		return user != null;
	}

	@Override
	public UserGroup createGroup(UserGroup usergroup) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		Group group = dao.getGroup(usergroup.getName());

		if (group == null) {
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

		if (group != null) {
			dao.delete(group);
		} else {
			return false;
		}

		return true;
	}

	@Override
	public List<HTUser> getAllUsers() {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		List<User> users = dao.getAllUsers();

		List<HTUser> htusers = new ArrayList<>();

		if (users != null)
			for (User user : users) {
				htusers.add(get(user, true));
			}

		return htusers;
	}

	@Override
	public List<UserGroup> getAllGroups() {
		UserGroupDaoImpl dao = DB.getUserGroupDao();

		return getFromDb(dao.getAllGroups());
	}

	@Override
	public UserGroup getGroupById(String groupId) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();

		return get(dao.getGroup(groupId));
	}

	@Override
	public List<HTUser> getUsersForGroups(String[] groups) {
		if (groups == null || groups.length == 0) {
			return new ArrayList<>();
		}

		List<HTUser> users = new ArrayList<>();
		for (String groupId : groups) {
			users.addAll(getUsersForGroup(groupId));
		}

		return users;
	}

	@Override
	public boolean updatePassword(String username, String password) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		User user = dao.getUser(username);
		user.setPassword(password);
		dao.save(user);

		return true;
	}

	@Override
	public List<Organization> getAllOrganizations() {
		List<Organization> organizations = new ArrayList<>();
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		List<Org> orgs = dao.getAllOrgs();

		for (Org org : orgs) {
			organizations.add(getOrg(org));
		}

		return organizations;
	}

	public Organization getOrg(Org org) {

		if (org == null) {
			return null;
		}

		Organization organization = new Organization();
		organization.setName(org.getName());
		organization.setId(org.getId());
		organization.setFullName(org.getFullName());

		return organization;
	}

	@Override
	public Organization createOrganization(Organization organization) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();

		Org org = dao.getOrganizationByName(organization.getName());

		if (org == null) {
			org = new Org();
		}

		org.setName(organization.getName());
		org.setFullName(organization.getFullName());
		dao.save(org);

		return organization;
	}

	@Override
	public void deleteOrganizaton(Organization organization) {
		UserGroupDaoImpl dao = DB.getUserGroupDao();
		Org org = dao.getOrganizationByName(organization.getName());

		if (org == null) {
			dao.delete(org);
		}
	}

}
