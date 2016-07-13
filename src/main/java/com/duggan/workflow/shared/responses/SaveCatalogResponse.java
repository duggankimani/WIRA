package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.catalog.Catalog;
import com.wira.commons.shared.response.BaseResponse;

public class SaveCatalogResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Catalog catalog;

	public SaveCatalogResponse() {
	}

	public SaveCatalogResponse(Catalog catalog) {
		this.catalog = catalog;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
}
