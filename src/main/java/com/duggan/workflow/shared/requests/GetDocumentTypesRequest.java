package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;

public class GetDocumentTypesRequest extends
		BaseRequest<GetDocumentTypesResponse> {

	public GetDocumentTypesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {

		return new GetDocumentTypesResponse();
	}
}
