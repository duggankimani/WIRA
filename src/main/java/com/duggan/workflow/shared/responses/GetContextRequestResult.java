package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.REPORTVIEWIMPL;
import com.wira.commons.shared.models.Version;
import com.wira.commons.shared.response.BaseResponse;

public class GetContextRequestResult extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean isValid;
	private CurrentUserDto currentUserDto;
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

	public CurrentUserDto getCurrentUserDto() {
		return currentUserDto;
	}

	public void setCurrentUserDto(CurrentUserDto currentUserDto) {
		this.currentUserDto = currentUserDto;
	}

}
