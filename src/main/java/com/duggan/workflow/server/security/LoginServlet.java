package com.duggan.workflow.server.security;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.wira.login.shared.request.LoginRequest;
import com.wira.login.shared.response.LoginRequestResult;

public class LoginServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		initRequest(req, resp);
	}
	
	@Override
	protected void executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

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
		
		new UserDaoHelper().execLogin(action, result);
		
		if(result.getCurrentUserDto()!=null && result.getCurrentUserDto().isLoggedIn()){
			logger.debug("#LoginServlet redirecting to "+app_page);
			redirectToApp();
		}else{
			logger.debug("#LoginServlet failed to login in; invalidating session");
			resp.setStatus(403);//NOT AUTHENTICATED
			req.getSession().invalidate();
		}
	
	}

	private void redirectToApp() throws ServletException, IOException {
		SessionHelper.getHttpResponse().sendRedirect(app_page);
	}

//	public void execLogin(LoginRequest action, LoginRequestResult result) {
//		HTUser userDto;
//		boolean isLoggedIn = true;
//
//		if (action.getActionType() == ActionType.VIA_COOKIE) {
//			logger.info("ActionType VIA_COOKIE LogInHandlerexecut(): loggedInCookie="
//					+ action.getLoggedInCookie());
//			userDto = getUserFromCookie(action.getLoggedInCookie());
//		} else {
//			userDto = getUserFromCredentials(action.getUsername(),
//					action.getPassword());
//		}
//
//		String contextPath = getContextPath();
//		logger.info("#Exec Login: Setting context path = " + contextPath);
//
//		isLoggedIn = userDto != null;
//
//		String loggedInCookie = "";
//		if (isLoggedIn) {
//			HttpSession session = SessionHelper.getHttpRequest().getSession(true);
//			loggedInCookie = createSessionCookie(action.getLoggedInCookie(),
//					userDto);
//			session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE,
//					loggedInCookie);
//			Cookie xsrfCookie = new Cookie(
//					ServerConstants.AUTHENTICATIONCOOKIE, loggedInCookie);
//			xsrfCookie.setPath(contextPath);
//			xsrfCookie.setMaxAge(3600 * 24 * 30); // Time in seconds (30 days)
//			xsrfCookie.setSecure(false); // http(s) cookie
//			SessionHelper.getHttpResponse().addCookie(xsrfCookie);
//		} else {
//			// reset headers
//			SessionHelper.getHttpResponse().setStatus(403); //Unauthorized
//			resetSession();
//		}
//
//		CurrentUserDto currentUserDto = new CurrentUserDto(isLoggedIn, userDto);
//
//		logger.info("LogInHandlerexecut(): actiontype="
//				+ action.getActionType());
//		logger.info("LogInHandlerexecut(): currentUserDto=" + currentUserDto);
//		logger.info("LogInHandlerexecut(): loggedInCookie=" + loggedInCookie);
//
//		assert action.getActionType() == null;
//
//		result.setValues(action.getActionType(), currentUserDto, loggedInCookie);
//	}
//
//	private String getContextPath() {
//		
//		String contextPath = SessionHelper.getHttpRequest().getContextPath();
//
//		if (!contextPath.isEmpty() && !contextPath.equals("/")) {
//			contextPath = (contextPath.startsWith("/") ? "" : "/")
//					+ contextPath + "/";
//		} else {
//			contextPath = "/";
//		}
//		return contextPath;
//	}
//
//	public void resetSession() {
//
//		HttpSession session = SessionHelper.getHttpRequest().getSession(false);
//		if (session != null) {
//			session.invalidate();
//		}
//		SessionHelper.getHttpResponse().setHeader(
//				"Set-Cookie",
//				ServerConstants.AUTHENTICATIONCOOKIE + "=deleted; path="
//						+ getContextPath() + "; "
//						+ "expires=Thu, 01 Jan 1970 00:00:00 GMT");
//	}
//
//	private String createSessionCookie(String loggedInCookie, HTUser user) {
//
//		HttpSession session = SessionHelper.getHttpRequest().getSession();
//		Object sessionId = session
//				.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
//		if (sessionId == null) {
//			sessionId = UUID.randomUUID().toString();
//		}
//		session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE, sessionId);
//		session.setAttribute(ServerConstants.USER, user);
//
//		return sessionId.toString();
//	}
//
//	private HTUser getUserFromCookie(String loggedInCookie) {
//
//		HTUser user = null;
//		HttpSession session = SessionHelper.getHttpRequest().getSession(false);
//		if (session == null) {
//			logger.info("getUserFromCookie(cookie=" + loggedInCookie
//					+ ") Session is null!!");
//			return null;
//		}
//
//		Object sessionId = session
//				.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
//		Object sessionUser = session.getAttribute(ServerConstants.USER);
//
//		boolean isValid = (session != null && sessionUser != null
//				&& sessionId != null && sessionId.equals(loggedInCookie));
//
//		if (isValid) {
//			user = (HTUser) sessionUser;
//			user.setGroups((ArrayList<UserGroup>) LoginHelper.get()
//					.getGroupsForUser(user.getUserId()));
//		}
//
//		logger.info("getUserFromCookie(cookie=" + loggedInCookie
//				+ ") Server sessionId= " + sessionId + ", validity = "
//				+ isValid + ", User = " + user);
//
//		return user;
//	}
//
//	private HTUser getUserFromCredentials(String username, String password) {
//		HTUser user = null;
//		boolean loggedIn = LoginHelper.get().login(username, password);
//		if (loggedIn) {
//			user = LoginHelper.get().getUser(username, true);
//		}
//
//		return user;
//	}

}
