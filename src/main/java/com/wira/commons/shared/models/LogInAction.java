package com.wira.commons.shared.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.wira.login.shared.model.ActionType;

@XmlRootElement
public class LogInAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionType actionType;
	private String username;
	private String password;
	private String loggedInCookie;
	private String apiKey;

	public LogInAction() {
	}

	public LogInAction(String username, String password) {
		actionType = ActionType.VIA_CREDENTIALS;
		this.password = password;
		this.username = username;
	}

	public LogInAction(String loggedInCookie) {
		actionType = ActionType.VIA_COOKIE;
		this.loggedInCookie = loggedInCookie;
	}

	public void setApiKeyLogin(String apiKey) {
		actionType = ActionType.VIA_APIKEY;
		this.apiKey = apiKey;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getLoggedInCookie() {
		return loggedInCookie;
	}
}
