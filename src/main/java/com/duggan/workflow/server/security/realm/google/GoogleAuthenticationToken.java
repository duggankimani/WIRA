package com.duggan.workflow.server.security.realm.google;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Google Authentication Token
 * 
 * @author duggan
 *
 */
public class GoogleAuthenticationToken implements AuthenticationToken{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String authCode;//Authentication
	private String idToken;//Verification

	public GoogleAuthenticationToken() {
		
	}
	
	public GoogleAuthenticationToken(String authCode) {
		this.authCode = authCode;
	}
	
	/**
	 * No Principal - username since google does the authentication
	 */
	@Override
	public Object getPrincipal() {
		return null;
	}


	/**
	 * No credentials since google does authentication
	 * @return
	 */
	@Override
	public Object getCredentials() {
		return null;
	}

	/**
	 * Authentication token from google
	 * @return
	 */
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}	
	
}
