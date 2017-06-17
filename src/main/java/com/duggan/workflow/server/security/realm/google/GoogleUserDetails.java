package com.duggan.workflow.server.security.realm.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.wira.commons.shared.models.HTUser;

public class GoogleUserDetails extends HTUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String locale;
	private boolean emailVerified;
	private String authCode;
	private String idToken;
	
	public GoogleUserDetails(Payload payload) {
		setEmail(payload.getEmail());
		emailVerified = Boolean.valueOf(payload.getEmailVerified());
		setFullName((String) payload.get("name"));
		setPictureUrl((String) payload.get("picture"));
		locale = (String) payload.get("locale");
		setSurname((String) payload.get("family_name"));
		setName((String) payload.get("given_name"));
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

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
