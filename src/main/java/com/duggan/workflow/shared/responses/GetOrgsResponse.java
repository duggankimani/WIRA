package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Org;

public class GetOrgsResponse extends BaseListResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Org> orgs;

	public GetOrgsResponse() {
	}

	public GetOrgsResponse(ArrayList<Org> orgs) {
		this.orgs = orgs;
	}

	public ArrayList<Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(ArrayList<Org> orgs) {
		this.orgs = orgs;
	}
	
}
