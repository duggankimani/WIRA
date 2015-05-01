package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDataResponse;

public class GetDataRequest extends BaseRequest<GetDataResponse> {

	private Long catalogId;

	public GetDataRequest() {
	}
	
	public GetDataRequest(Long catalogId) {
		this.catalogId = catalogId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDataResponse();
	}

	public Long getCatalogId() {
		return catalogId;
	}
}
