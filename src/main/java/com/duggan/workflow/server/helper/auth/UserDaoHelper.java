package com.duggan.workflow.server.helper.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.commands.SendMailCommand;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.dao.UserDaoImpl;
import com.duggan.workflow.server.dao.model.Activation;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.PermissionModel;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.CustomEmailHandler;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.security.authenticator.AuthenticationException;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.LogInAction;
import com.wira.commons.shared.models.LogInResult;
import com.wira.commons.shared.models.PermissionPOJO;
import com.wira.commons.shared.models.UserGroup;

public class UserDaoHelper implements LoginIntf {

	Logger logger = Logger.getLogger(UserDaoHelper.class.getName());

	@Override
	public void close() throws IOException {

	}

	@Override
	public boolean login(String username, String password) {

		User user = DB.getUserDao().getUser(username);

		return user != null && user.getPassword().equals(password);
	}

	@Override
	public List<HTUser> retrieveUsers() {
		List<HTUser> ht_users = new ArrayList<>();

		List<User> users = DB.getUserDao().getAllUsers(null);

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
		htuser.setRefId(user.getRefId());
		htuser.setPictureUrl(user.getPictureUrl());

		if (loadGroups) {
			htuser.setGroups(getFromDb(user.getGroups()));
			JBPMHelper.get().setCounts(htuser);
		}

		return htuser;
	}

	private ArrayList<UserGroup> getFromDb(Collection<Group> groups) {
		List<UserGroup> userGroups = new ArrayList<>();

		if (groups != null)
			for (Group group : groups) {
				userGroups.add(get(group));
			}

		return (ArrayList<UserGroup>) userGroups;
	}

	@Override
	public HTUser getUser(String userId) {
		return getUser(userId, false);
	}

	@Override
	public HTUser getUser(String userId, boolean loadGroups) {
		User user = DB.getUserDao().getUser(userId);

		if (user != null) {
			return get(user, loadGroups);
		}

		return null;
	}

	@Override
	public List<UserGroup> retrieveGroups() {
		List<Group> groups = DB.getUserDao().getAllGroups(null);

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
	public HTUser createUser(HTUser htuser, boolean isSendActivationEmail) {

		boolean isNew = htuser.getId() == null;

		UserDaoImpl dao = DB.getUserDao();

		User user = new User();

		if (htuser.getId() != null) {
			user = dao.getUser(htuser.getUserId());
		}

		user.setId(htuser.getId());
		user.setArchived(false);
		user.setEmail(htuser.getEmail());
		user.setFirstName(htuser.getName());
		user.setLastName(htuser.getSurname());
		user.setUserId(htuser.getUserId());
		user.setGroups(get(htuser.getGroups()));
		user.setRefreshToken(htuser.getRefreshToken());
		user.setPictureUrl(htuser.getPictureUrl());

		dao.saveUser(user, false);

		htuser = get(user);
		if (isSendActivationEmail) {
			if (isNew) {
				sendActivationEmail(user);
			} else {
				sendAccountResetEmail(htuser);
			}

		}

		return htuser;
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
			UserDaoImpl dao = DB.getUserDao();
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

		User user = DB.getUserDao().getUser(htuser.getId());

		DB.getUserDao().delete(user);

		return true;
	}

	@Override
	public List<UserGroup> getGroupsForUser(String userId) {
		UserDaoImpl dao = DB.getUserDao();
		Collection<Group> groups = dao.getAllGroupsByUserId(userId);

		List<UserGroup> userGroups = new ArrayList<>();

		if (groups != null)
			for (Group group : groups) {
				userGroups.add(get(group));
			}

		return userGroups;
	}

	@Override
	public List<HTUser> getUsersForGroup(String groupName) {
		UserDaoImpl dao = DB.getUserDao();
		Collection<User> users = dao.getAllUsersByGroupId(groupName);

		List<HTUser> groupUsers = new ArrayList<>();

		if (users != null)
			for (User user : users) {
				groupUsers.add(get(user));
			}

		return groupUsers;
	}

	@Override
	public boolean existsUser(String userId) {
		UserDaoImpl dao = DB.getUserDao();
		User user = dao.getUser(userId);

		return user != null;
	}

	@Override
	public UserGroup createGroup(UserGroup usergroup) {
		UserDaoImpl dao = DB.getUserDao();
		Group group = dao.getGroup(usergroup.getName());

		if (group == null) {
			group = new Group();
		}

		group.setFullName(usergroup.getFullName());
		group.setName(usergroup.getName());
		group.setArchived(false);

		Set<PermissionModel> permissions = new HashSet<>();
		for (PermissionPOJO pojo : usergroup.getPermissions()) {
			permissions.add(DB.getPermissionDao().getPermissionByName(
					pojo.getName()));
		}
		group.setPermissions(permissions);

		dao.saveGroup(group);

		return get(group);
	}

	@Override
	public boolean deleteGroup(UserGroup userGroup) {
		UserDaoImpl dao = DB.getUserDao();

		Group group = dao.getGroup(userGroup.getName());

		if (group != null) {
			dao.delete(group);
		} else {
			return false;
		}

		return true;
	}

	@Override
	public List<HTUser> getAllUsers(String searchTerm) {
		UserDaoImpl dao = DB.getUserDao();
		List<User> users = dao.getAllUsers(searchTerm);

		List<HTUser> htusers = new ArrayList<>();

		if (users != null)
			for (User user : users) {
				htusers.add(get(user, true));
			}

		return htusers;
	}

	@Override
	public List<UserGroup> getAllGroups(String searchTerm) {
		UserDaoImpl dao = DB.getUserDao();

		return getFromDb(dao.getAllGroups(searchTerm));
	}

	@Override
	public UserGroup getGroupById(String groupId) {
		UserDaoImpl dao = DB.getUserDao();

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
		UserDaoImpl dao = DB.getUserDao();
		User user = dao.getUser(username);
		user.setPassword(password);
		dao.changePassword(user);

		return true;
	}

	public HTUser getUserByEmail(String email) {
		User user = DB.getUserDao().getUserByEmail(email);
		if (user == null) {
			return null;
		}

		return get(user);
	}

	private void sendActivationEmail(User user) {
		String subject = "Welcome to WIRA BPM!";
		Activation act = new Activation(user.getRefId());
		DB.getUserDao().save(act);
		
		String link = SessionHelper.getApplicationPath()
				+ "/account.html#/activateacc/"+act.getRefId()+"/"+ user.getRefId() + "/default";

		String body = "Dear "
				+ user.getFullNames()
				+ ","
				+ "<p/>An account has been created for you in the KNA Editorial portal. "
				+ "<a href=" + link + ">Click this link </a>"
				+ " to create your password." + "<p>Thank you";

		try {
			HTUser htuser = new HTUser(user.getUserId(), user.getEmail());
			List<HTUser> recipients = Arrays.asList(htuser);
			sendMail(subject, recipients, body);
		} catch (Exception e) {
			logger.warn("Activation Email for " + user.getEmail()
					+ " failed. Cause: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void sendMail(String subject, List<HTUser> recipients, String body) {
		CommandContext context = new CommandContext();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("callbacks", CommandCodes.SendEmailCallback.name());
		params.put("To", recipients);
		params.put("From", "Head KNA");
		params.put(SendMailCommand.SUBJECT, subject);
		params.put(SendMailCommand.BODY, body);
		context.setData(params);
		ExecutorModule.getInstance().getExecutorServiceEntryPoint()
				.scheduleRequest(CommandCodes.SendEmailCommand, context);

	}

	public void sendAccountResetEmail(HTUser user) {
		
		Activation act = new Activation(user.getRefId());
		DB.getUserDao().save(act);

		String subject = "WIRA Workflow Password Reset";
		String link = SessionHelper.getApplicationPath()
				+ "/account.html#/activateacc/" +act.getRefId()+"/"+ user.getRefId() + "/reset";

		String body = "Hello "
				+ user.getFullName()
				+ ","
				+ "<br/>We received a request to reset the password for your account."
				+ "<p>" + "If you requested a reset for " + user.getEmail()
				+ ", click the button below."
				+ " If you didn’t make this request, please ignore this email."
				+ "<p>" + "<a href=" + link + ">Click this link </a>"
				+ " to reset your password." + "<p>";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fullNames", user.getFullName());
		params.put("userEmail", user.getEmail());
		params.put("ResetUrl", link);
		params.put("fullNames", user.getFullName());

		try {
			body = IOUtils.toString(UserDaoHelper.class.getClassLoader()
					.getResourceAsStream("ewsWelcomeMsg.html"));

			// System.err.println(">>" + user.getEmail());
			HTUser htuser = new HTUser(user.getEmail(), user.getEmail());
			htuser.setName(user.getName());
			htuser.setSurname(user.getSurname());
			List<HTUser> recipients = Arrays.asList(htuser);

			new CustomEmailHandler()
					.sendMail(subject, body, recipients, params);
		} catch (Exception e) {
			logger.warn("Activation Email for " + user.getEmail()
					+ " failed. Cause: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void execLogin(LogInAction action, LogInResult result) {
		//Example using most common scenario of username/password pair:
		UsernamePasswordToken token = new UsernamePasswordToken(action.getUsername(), action.getPassword());
		//"Remember Me" built-in: 
		token.setRememberMe(true);
		boolean isAuthenticated = execLogin(token); 
		logger.info("After Authentication - isAuthenticated = "+isAuthenticated);
		wrapResult(isAuthenticated,result);
	}
	
	public boolean execLogin(AuthenticationToken token) {
		Subject currentUser = SecurityUtils.getSubject();
		
		try{
			currentUser.login(token);
		}catch(Exception e){
			logger.debug(e.getMessage());
		}
		
		return currentUser.isAuthenticated() || currentUser.isRemembered();
	}
	
	public void wrapResult(boolean loggedIn, LogInResult result) {
		result.setValues(result.getActionType(), new CurrentUserDto(loggedIn, SessionHelper.getCurrentUser()), null);
	}


	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		resetCookies();
	}

	private HTUser getUserFromCookie(String loggedInCookie) {
		HTUser userDto = null;
		
		HttpServletRequest request = SessionHelper.getHttpRequest();
		try {
			if (request.getSession(false) != null) {
				Object authCookie = request.getSession(false)
						.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
				if (authCookie == null || !authCookie.equals(loggedInCookie)) {
					return null;
				}

				userDto = (HTUser) request.getSession(false)
						.getAttribute(ServerConstants.USER);
			}
		} catch (AuthenticationException e) {
			// isLoggedIn = false;
		}

		return userDto;
	}

	private String getContextPath() {
//		String contextPath = request.get().getServletContext().getContextPath();
		String contextPath = SessionHelper.getHttpRequest().getContextPath();
		if (!contextPath.isEmpty() && !contextPath.equals("/")) {
			contextPath = (contextPath.startsWith("/") ? "" : "/") + contextPath
					+ "/";
		} else {
			contextPath = "/";
		}
		return contextPath;
	}
	
	public void resetCookies() {
		SessionHelper.getHttpResponse().setHeader("Set-Cookie",
				ServerConstants.AUTHENTICATIONCOOKIE + "=deleted; path="
						+ getContextPath() + "; "
						+ "expires=Thu, 01 Jan 1970 00:00:00 GMT");
	}


	private String createSessionCookie(String loggedInCookie, HTUser user) {

		HttpSession session = SessionHelper.getHttpRequest().getSession(true);
		Object sessionId = session.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		if(sessionId == null){
			sessionId = UUID.randomUUID().toString();
		} 
		session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE, sessionId);
		session.setAttribute(ServerConstants.USER, user);
		
		return sessionId.toString();
	}
	
	public static void importUsers(String name, long size,
			InputStream inputStream) throws IOException {
		
		Reader reader = new InputStreamReader(inputStream);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(reader);

		int row = 0;
		
		UserDaoImpl dao = DB.getUserDao();
		for (CSVRecord rec : records) {

			if (row++ == 0) {
				// Column Names
				continue;
			}

			int col = 0;

			String firstName = rec.get(col++);
			String lastName = rec.get(col++);
			String email = rec.get(col++).toLowerCase();

			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUserId(email);
			user.setEmail(email);
			
			Group group = dao.getGroup("Staff");
			if(group!=null){
				user.setGroups(Arrays.asList(group));
			}
			dao.save(user);
		}
	}
}
