package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Org;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveOrgResponse;

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
