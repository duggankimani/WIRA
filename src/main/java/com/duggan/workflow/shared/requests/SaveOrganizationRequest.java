package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Organization;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveOrganizationResponse;

public class SaveOrganizationRequest extends BaseRequest<SaveOrganizationResponse> {

	private Organization organization;
	private boolean isDelete = false;

	@SuppressWarnings("unused")
	private SaveOrganizationRequest() {
		// For serialization only
	}

	public SaveOrganizationRequest(Organization organization) {
		this.organization = organization;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {

		return new SaveOrganizationResponse();
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
