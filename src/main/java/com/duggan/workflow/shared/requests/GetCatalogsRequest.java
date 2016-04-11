package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;

public class GetCatalogsRequest extends BaseRequest<GetCatalogsResponse> {

	private Long catalogId;
	private boolean isLoadViews;
	
	public GetCatalogsRequest() {
	}
	
	public GetCatalogsRequest(Long catalogId) {
		this.catalogId = catalogId;
	}
	
	public GetCatalogsRequest(boolean isLoadViews) {
		this.isLoadViews = isLoadViews;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetCatalogsResponse();
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public boolean isLoadViews() {
		return isLoadViews;
	}

}
