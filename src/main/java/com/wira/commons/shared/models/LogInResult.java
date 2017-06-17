package com.wira.commons.shared.models;

import java.io.Serializable;

import com.wira.login.shared.model.ActionType;

public class LogInResult extends SerializableObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionType actionType;
	private CurrentUserDto currentUserDto;
	private String loggedInCookie;
	private Version version;

	public LogInResult() {
		// Needed for serialization
	}

	public LogInResult(ActionType actionType, CurrentUserDto currentUserDto, String loggedInCookie) {
		this.actionType = actionType;
		this.currentUserDto = currentUserDto;
		this.loggedInCookie = loggedInCookie;
	}

	public LogInResult(ActionType actionType, CurrentUserDto currentUserDto, String loggedInCookie, Version version) {
		this.actionType = actionType;
		this.currentUserDto = currentUserDto;
		this.loggedInCookie = loggedInCookie;
		this.version = version;
	}

	public CurrentUserDto getCurrentUserDto() {
		return currentUserDto;
	}

	public String getLoggedInCookie() {
		return loggedInCookie;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public void setCurrentUserDto(CurrentUserDto currentUserDto) {
		this.currentUserDto = currentUserDto;
	}

	public void setLoggedInCookie(String loggedInCookie) {
		this.loggedInCookie = loggedInCookie;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
	
	public void setValues(ActionType actionType, CurrentUserDto currentUserDto,
			String loggedInCookie) {
		this.actionType = actionType;
		this.currentUserDto = currentUserDto;
		this.loggedInCookie = loggedInCookie;
	}
}
