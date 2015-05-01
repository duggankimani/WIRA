package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;

public class GetCatalogsRequest extends BaseRequest<GetCatalogsResponse> {

	private Long catalogId;

	public GetCatalogsRequest() {
	}
	
	public GetCatalogsRequest(Long catalogId) {
		this.catalogId = catalogId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetCatalogsResponse();
	}

	public Long getCatalogId() {
		return catalogId;
	}
}
