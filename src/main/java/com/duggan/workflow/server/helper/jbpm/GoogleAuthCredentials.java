package com.duggan.workflow.server.helper.jbpm;


public class GoogleAuthCredentials {

	private String clientId;
	private String clientSecret;
	private String projectId;
	private String authUri;
	private String tokenUri;
	private String[] redirectUris;
	private String[] javascriptOrigins;
	
	public GoogleAuthCredentials() {
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getAuthUri() {
		return authUri;
	}

	public void setAuthUri(String authUri) {
		this.authUri = authUri;
	}

	public String getTokenUri() {
		return tokenUri;
	}

	public void setTokenUri(String tokenUri) {
		this.tokenUri = tokenUri;
	}

	public String[] getRedirectUris() {
		return redirectUris;
	}

	public void setRedirectUris(String[] redirectUris) {
		this.redirectUris = redirectUris;
	}

	public String[] getJavascriptOrigins() {
		return javascriptOrigins;
	}

	public void setJavascriptOrigins(String[] javascriptOrigins) {
		this.javascriptOrigins = javascriptOrigins;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
}
