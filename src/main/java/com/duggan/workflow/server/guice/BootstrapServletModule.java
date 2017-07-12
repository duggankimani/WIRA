package com.duggan.workflow.server.guice;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.guice.web.GuiceShiroFilter;

import com.duggan.workflow.server.security.GoogleIdTokenVerifierService;
import com.duggan.workflow.server.security.GoogleLoginCallbackServlet;
import com.duggan.workflow.server.security.GoogleLoginServlet;
import com.duggan.workflow.server.security.LoginServlet;
import com.duggan.workflow.server.security.shiro.ShiroSecurityModule;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.rpc.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

/**
 * This class bootstraps the application Servlet (Jersey 1.18.1).
 * 
 * @author pablo.biagioli
 *
 */
public class BootstrapServletModule extends ServletModule {


	@Override
	protected void configureServlets() {

		// Initialize Apache Shiro if present
		install(new ShiroSecurityModule(getServletContext()));

		// if you had a ShiroWebModule installed above you would need to add
		// this GuiceShiroFilter also.
		filter("/*").through(GuiceShiroFilter.class);
		
		serve("/api/" + ActionImpl.DEFAULT_SERVICE_NAME+"*").with(DispatchServiceImpl.class);
		
		// GOOGLE AUTH
		Map<String, String> authParams = new HashMap<String, String>();
		authParams.put("app_page", "/index.html");
		authParams.put("login_page", "/login.html");
		authParams.put("loadonstartup", "1");
		serve("/api/auth2").with(LoginServlet.class, authParams);
		serve("/api/googleservlet").with(GoogleLoginServlet.class,authParams);
		serve("/api/oauth2callback").with(GoogleLoginCallbackServlet.class,authParams);
		serve("/api/auth2tokencallback").with(
				GoogleIdTokenVerifierService.class,authParams);
		
//		Map<String, String> params2 = new HashMap<String, String>();
//		params2.put("loadonstartup", "1");
//		serve("/files/*").with(FileManagementServlet.class, params2);//No TRansactions created before hand!
//		serve("/files").with(FileManagementServlet.class, params2);
		
	}
}
