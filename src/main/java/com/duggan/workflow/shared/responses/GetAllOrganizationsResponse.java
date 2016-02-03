package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.Organization;

public class GetAllOrganizationsResponse extends BaseResponse {

	private List<Organization> organizations;

	public GetAllOrganizationsResponse() {
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

}
