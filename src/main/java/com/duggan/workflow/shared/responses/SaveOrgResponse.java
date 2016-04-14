package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Org;

public class SaveOrgResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Org org;

	public SaveOrgResponse() {
		// For serialization only
	}

	public SaveOrgResponse(Org org) {
		this.org = org;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}
}
