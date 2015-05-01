package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.catalog.Catalog;


public class GetCatalogsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Catalog> catalogs;

	public GetCatalogsResponse() {
		// For serialization only
	}

	public GetCatalogsResponse(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}

	public List<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}
}
