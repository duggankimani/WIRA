package com.duggan.workflow.server.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SecurityFilter implements Filter {

	@Inject
	private Provider<HttpServletRequest> requestProvider;
	@Inject
	private Provider<HttpServletResponse> responseProvider;

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			showFailedLogin();
		}
	}

	private void showFailedLogin() throws IOException {
		HttpServletResponse response = responseProvider.get();
		response.setStatus(401);
//		response.setContentType("text/html");
		String context = requestProvider.get().getContextPath();
		response.sendRedirect(context+"/login.html");
		
		response
				.getOutputStream()
				.write("Forbidden. Please login to access this api.".getBytes());
	}

	@Override
	public void destroy() {

	}

}
