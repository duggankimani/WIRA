package com.duggan.workflow.server.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;
import com.wira.login.shared.model.ActionType;
import com.wira.login.shared.request.LoginRequest;
import com.wira.login.shared.response.LoginRequestResult;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	Logger logger = Logger.getLogger(LoginServlet.class);
	String app_page = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (config != null) {
			app_page = config.getInitParameter("app_page");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		request.set(req);
		response.set(resp);

		LoginRequest action = null;
		LoginRequestResult result = new LoginRequestResult();
		
		String loginMethod = req.getParameter("loginmethod");
		String authCookie = "";
		if(loginMethod!=null && loginMethod.equals("VIA_COOKIE")){
			for(Cookie cookie: req.getCookies()){
				if(cookie.getName().equals(ServerConstants.AUTHENTICATIONCOOKIE)){
					authCookie= cookie.getValue();
				}
			}
			action = new LoginRequest(authCookie);
		}else{

			String username = req.getParameter("username");
			String password  = req.getParameter("password");
			action = new LoginRequest(username, password);
		}
		
		execLogin(action, result);
		
		if(result.getCurrentUserDto()!=null && result.getCurrentUserDto().isLoggedIn()){
			logger.debug("#LoginServlet redirecting to "+app_page);
			redirectToApp();
		}else{
			logger.debug("#LoginServlet failed to login in; invalidating session");
			req.getSession().invalidate();
		}
		
	}

	private void redirectToApp() throws ServletException, IOException {
		request.get().getRequestDispatcher(app_page).forward(request.get(), response.get());
	}

	public void execLogin(LoginRequest action, LoginRequestResult result) {
		HTUser userDto;
		boolean isLoggedIn = true;

		if (action.getActionType() == ActionType.VIA_COOKIE) {
			logger.info("ActionType VIA_COOKIE LogInHandlerexecut(): loggedInCookie="
					+ action.getLoggedInCookie());
			userDto = getUserFromCookie(action.getLoggedInCookie());
		} else {
			userDto = getUserFromCredentials(action.getUsername(),
					action.getPassword());
		}

		String contextPath = getContextPath();
		logger.info("#Exec Login: Setting context path = " + contextPath);

		isLoggedIn = userDto != null;

		String loggedInCookie = "";
		if (isLoggedIn) {
			HttpSession session = request.get().getSession(true);
			loggedInCookie = createSessionCookie(action.getLoggedInCookie(),
					userDto);
			session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE,
					loggedInCookie);
			Cookie xsrfCookie = new Cookie(
					ServerConstants.AUTHENTICATIONCOOKIE, loggedInCookie);
			xsrfCookie.setHttpOnly(false);// http only
			xsrfCookie.setPath(contextPath);
			xsrfCookie.setMaxAge(3600 * 24 * 30); // Time in seconds (30 days)
			xsrfCookie.setSecure(false); // http(s) cookie
			response.get().addCookie(xsrfCookie);
		} else {
			// reset headers
			response.get().setStatus(403); //Unauthorized
			resetSession();
		}

		CurrentUserDto currentUserDto = new CurrentUserDto(isLoggedIn, userDto);

		logger.info("LogInHandlerexecut(): actiontype="
				+ action.getActionType());
		logger.info("LogInHandlerexecut(): currentUserDto=" + currentUserDto);
		logger.info("LogInHandlerexecut(): loggedInCookie=" + loggedInCookie);

		assert action.getActionType() == null;

		result.setValues(action.getActionType(), currentUserDto, loggedInCookie);
	}

	private String getContextPath() {
		String contextPath = request.get().getServletContext().getContextPath();

		if (!contextPath.isEmpty() && !contextPath.equals("/")) {
			contextPath = (contextPath.startsWith("/") ? "" : "/")
					+ contextPath + "/";
		} else {
			contextPath = "/";
		}
		return contextPath;
	}

	public void resetSession() {

		HttpSession session = request.get().getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.get().setHeader(
				"Set-Cookie",
				ServerConstants.AUTHENTICATIONCOOKIE + "=deleted; path="
						+ getContextPath() + "; "
						+ "expires=Thu, 01 Jan 1970 00:00:00 GMT");
	}

	private String createSessionCookie(String loggedInCookie, HTUser user) {

		HttpSession session = request.get().getSession(true);
		Object sessionId = session
				.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		if (sessionId == null) {
			sessionId = UUID.randomUUID().toString();
		}
		session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE, sessionId);
		session.setAttribute(ServerConstants.USER, user);

		return sessionId.toString();
	}

	private HTUser getUserFromCookie(String loggedInCookie) {

		HTUser user = null;
		HttpSession session = request.get().getSession(false);
		if (session == null) {
			logger.info("getUserFromCookie(cookie=" + loggedInCookie
					+ ") Session is null!!");
			return null;
		}

		Object sessionId = session
				.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		Object sessionUser = session.getAttribute(ServerConstants.USER);

		boolean isValid = (session != null && sessionUser != null
				&& sessionId != null && sessionId.equals(loggedInCookie));

		if (isValid) {
			user = (HTUser) sessionUser;
			user.setGroups((ArrayList<UserGroup>) LoginHelper.get()
					.getGroupsForUser(user.getUserId()));
		}

		logger.info("getUserFromCookie(cookie=" + loggedInCookie
				+ ") Server sessionId= " + sessionId + ", validity = "
				+ isValid + ", User = " + user);

		return user;
	}

	private HTUser getUserFromCredentials(String username, String password) {
		HTUser user = null;
		boolean loggedIn = LoginHelper.get().login(username, password);
		if (loggedIn) {
			user = LoginHelper.get().getUser(username, true);
		}

		return user;
	}

}
