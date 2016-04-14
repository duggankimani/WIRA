package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.Org;

public class GetOrgsResponse extends BaseListResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Org> orgs;

	public GetOrgsResponse() {
	}

	public GetOrgsResponse(List<Org> orgs) {
		this.orgs = orgs;
	}

	public List<Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<Org> orgs) {
		this.orgs = orgs;
	}
	
}
