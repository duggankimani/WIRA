package com.duggan.workflow.server.security.realm.google;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class GoogleAuthenticationInfo implements AuthenticationInfo{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PrincipalCollection principalCollection;
	
	/**
	 * The principal will hold the email, & GoogleUserDetails object
	 * 
	 * @param googleUserDetails
	 * @param realmName
	 */
	public GoogleAuthenticationInfo(GoogleUserDetails googleUserDetails, String accessToken, String realmName) {
		Collection<Object> principals = new ArrayList<Object>();
		principals.add(googleUserDetails);
		principals.add(googleUserDetails.getEmail());
		principals.add(accessToken);
		this.principalCollection = new SimplePrincipalCollection(principals, realmName);
	}

	@Override
	public PrincipalCollection getPrincipals() {
		return principalCollection;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	
}
