package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveCatalogResponse;

public class SaveCatalogRequest extends BaseRequest<SaveCatalogResponse> {

	private Catalog catalog;

	@SuppressWarnings("unused")
	private SaveCatalogRequest() {
		// For serialization only
	}

	public SaveCatalogRequest(Catalog catalog) {
		this.catalog = catalog;
	}

	public Catalog getCatalog() {
		return catalog;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveCatalogResponse();
	}
}
