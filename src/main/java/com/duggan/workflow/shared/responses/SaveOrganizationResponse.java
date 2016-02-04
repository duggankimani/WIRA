package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Organization;

public class SaveOrganizationResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Organization organization;

	public SaveOrganizationResponse() {
		// For serialization only
	}

	public SaveOrganizationResponse(Organization organization) {
		this.organization = organization;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
