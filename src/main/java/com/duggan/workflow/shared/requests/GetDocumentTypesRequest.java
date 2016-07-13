package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetDocumentTypesRequest extends
		BaseRequest<GetDocumentTypesResponse> {

	public GetDocumentTypesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {

		return new GetDocumentTypesResponse();
	}
}
