package com.duggan.workflow.server.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.ServerConstants;

public class SecurityFilter implements Filter {

	String loginPage;

	Logger logger = Logger.getLogger(SecurityFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (filterConfig != null) {
			loginPage = filterConfig.getInitParameter("login_page");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (loginPage == null) {
			logger.fatal("AuthorizationFilter not "
					+ "properly configured! Contact Administrator.");
			returnError(request, response, "AuthorizationFilter not "
					+ "properly configured! Contact Administrator.");
		}

		HttpSession session = ((HttpServletRequest) request).getSession(false);
		Object authCookie = null;
		if(session==null){
			logger.debug("A session does not exist for this request!");
			returnError(request, response, "A session does not exist for this request!");
			return;
		}else{
			authCookie = session.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		}
		
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		String reqAuthCookie = null;
		for(Cookie c: cookies){
			if(c.getName().equals(ServerConstants.AUTHENTICATIONCOOKIE)){
				reqAuthCookie = c.getValue();
			}
		}
		
		
		if (authCookie == null || reqAuthCookie==null) {
			logger.debug("Authentication cookie not found in the request or session!");
			returnError(request, response, "Authentication cookie not found in the request or session!");
		}else if(!reqAuthCookie.equals(authCookie)){
			logger.debug("Authentication cookie does not match! - invalidating session!");
			session.invalidate(); //Just incase FE tries to validate its cookie - via /dispatcher/login
			((HttpServletResponse)response).setHeader("Set-Cookie",
					ServerConstants.AUTHENTICATIONCOOKIE + "=deleted; path="
							+ getContextPath((HttpServletRequest)request) + "; "
							+ "expires=Thu, 01 Jan 1970 00:00:00 GMT");
			returnError(request, response, "Invalid request cookie found");
		}else {
			logger.debug("#Security Filter - Authentication success! - forwarding request");
			chain.doFilter(request, response);
		}
	}
	
	private String getContextPath(HttpServletRequest request) {
		String contextPath = request.getServletContext().getContextPath();

		if (!contextPath.isEmpty() && !contextPath.equals("/")) {
			contextPath = (contextPath.startsWith("/") ? "" : "/") + contextPath
					+ "/";
		} else {
			contextPath = "/";
		}
		return contextPath;
	}


	/** Accepts error string, forwards to error page with error. */
	private void returnError(ServletRequest request, ServletResponse aResponse,
			String errorString) throws ServletException, IOException {
		HttpServletResponse response = ((HttpServletResponse)aResponse); 
		response.setStatus(403);
		response.setContentType("text/html");
		response.getWriter().print(errorString);
		response.sendRedirect(loginPage);
	}

	@Override
	public void destroy() {

	}

}
