package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.model.Version;
import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;

public class GetContextRequestResult extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean isValid;
	private HTUser user;
	private List<UserGroup> groups;
	private Version version;
	private String organizationName;
	private REPORTVIEWIMPL reportViewImpl;

	public GetContextRequestResult() {
		// For serialization only
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public REPORTVIEWIMPL getReportViewImpl() {
		return reportViewImpl;
	}

	public void setReportViewImpl(REPORTVIEWIMPL reportViewImpl) {
		this.reportViewImpl = reportViewImpl;
	}

}
