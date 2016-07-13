package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.SaveOrgResponse;
import com.wira.commons.shared.models.Org;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveOrgRequest extends BaseRequest<SaveOrgResponse> {

	private Org org;
	private boolean isDelete=false;

	@SuppressWarnings("unused")
	private SaveOrgRequest() {
		// For serialization only
	}

	public SaveOrgRequest(Org org) {
		this.org = org;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new SaveOrgResponse();
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}
	
	
}
