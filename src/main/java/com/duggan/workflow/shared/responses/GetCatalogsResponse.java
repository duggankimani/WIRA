package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.catalog.Catalog;
import com.wira.commons.shared.response.BaseResponse;


public class GetCatalogsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Catalog> catalogs;

	public GetCatalogsResponse() {
		// For serialization only
	}

	public GetCatalogsResponse(ArrayList<Catalog> catalogs) {
		this.catalogs = catalogs;
	}

	public ArrayList<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(ArrayList<Catalog> catalogs) {
		this.catalogs = catalogs;
	}
}
