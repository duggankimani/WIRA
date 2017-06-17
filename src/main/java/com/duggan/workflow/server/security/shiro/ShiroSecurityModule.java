package com.duggan.workflow.server.security.shiro;

import javax.servlet.ServletContext;

import org.apache.shiro.guice.web.ShiroWebModule;

import com.duggan.workflow.server.security.SecurityFilter;
import com.duggan.workflow.server.security.realm.DBAuthenticatingRealm;
import com.duggan.workflow.server.security.realm.GoogleOAuthAuthentingRealm;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.google.inject.Key;
import com.wira.commons.shared.models.PermissionName;

public class ShiroSecurityModule extends ShiroWebModule {

	public ShiroSecurityModule(ServletContext context) {
		super(context);
	}

	@SuppressWarnings("unchecked")
	protected void configureShiroWeb() {
		bindRealm().to(DBAuthenticatingRealm.class);
		bindRealm().to(GoogleOAuthAuthentingRealm.class);

		// addFilterChain("/api/**", AUTHC_BASIC);
		bind(SecurityFilter.class);
		Key<SecurityFilter> customFilter = Key.get(SecurityFilter.class);
		
		addFilterChain("/logout", LOGOUT);

		addFilterChain("/api/auth2", ANON);
		addFilterChain("/api/auth2/**", ANON);
		addFilterChain("/api/auth2tokencallback", ANON);
		addFilterChain("/api/oauth2callback", ANON);
		addFilterChain("/api/googleservlet", ANON);

		//Each request goes to a unique url, can be protected like below:
		//addFilterChain(new GetProcessesRequest(false).getServiceName(), config(REST, "Processes"));

		// ALL Others
		addFilterChain("/api/**", AUTHC);

		addFilterChain("/getreport", AUTHC);
		addFilterChain("/index.html", AUTHC);
	}

}
