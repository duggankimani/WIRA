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
import javax.servlet.http.HttpSession;

import com.duggan.workflow.server.ServerConstants;

public class SecurityFilter implements Filter {

	String loginPage;

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
			returnError(request, response, "AuthorizationFilter not "
					+ "properly configured! Contact Administrator.");
		}

		HttpSession session = ((HttpServletRequest) request).getSession(false);
		Object authCookie = null;
		if(session==null){
			returnError(request, response, "User does not exist in session!");
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
			returnError(request, response, "Session does not exist");
		}else if(!reqAuthCookie.equals(authCookie)){
			returnError(request, response, "Invalid request cookie presented");
		}else {
			chain.doFilter(request, response);
		}
	}

	/** Accepts error string, forwards to error page with error. */
	private void returnError(ServletRequest request, ServletResponse response,
			String errorString) throws ServletException, IOException {
		request.setAttribute("error", errorString);
		request.getRequestDispatcher(loginPage).forward(request, response);
	}

	@Override
	public void destroy() {

	}

}
