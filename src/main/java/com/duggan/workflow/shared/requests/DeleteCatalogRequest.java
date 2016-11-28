package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.catalog.IsCatalogItem;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteCatalogRequest extends
		BaseRequest<BaseResponse> {

	private IsCatalogItem item;

	@SuppressWarnings("unused")
	private DeleteCatalogRequest() {
	}

	public DeleteCatalogRequest(IsCatalogItem item) {
		this.item = item;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {		
		return new BaseResponse();
	}

	public IsCatalogItem getItem() {
		return item;
	}
}
