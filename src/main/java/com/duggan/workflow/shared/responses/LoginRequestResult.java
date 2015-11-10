package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.ActionType;
import com.duggan.workflow.shared.model.CurrentUserDto;
import com.duggan.workflow.shared.model.Version;
import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;

public class LoginRequestResult extends BaseResponse {

	private ActionType actionType;
	private CurrentUserDto currentUserDto;
	private String loggedInCookie;
	private REPORTVIEWIMPL reportView;
	private String organizationName;
	private Version version;

	@SuppressWarnings("unused")
	public LoginRequestResult() {
		// For serialization only
	}

	public void setValues(ActionType actionType, CurrentUserDto currentUserDto,
			String loggedInCookie) {
				this.actionType = actionType;
				this.currentUserDto = currentUserDto;
				this.loggedInCookie = loggedInCookie;
		
	}

	public ActionType getActionType() {
		return actionType;
	}

	public CurrentUserDto getCurrentUserDto() {
		return currentUserDto;
	}

	public String getLoggedInCookie() {
		return loggedInCookie;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public void setReportViewImpl(REPORTVIEWIMPL reportView) {
		this.reportView = reportView;
	}

	public REPORTVIEWIMPL getReportView() {
		return reportView;
	}

	public void setReportView(REPORTVIEWIMPL reportView) {
		this.reportView = reportView;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public Version getVersion() {
		return version;
	}
}
