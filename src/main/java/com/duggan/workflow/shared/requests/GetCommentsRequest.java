package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetCommentsRequest extends BaseRequest<GetCommentsResponse> {

	private String docRefId;

	@SuppressWarnings("unused")
	private GetCommentsRequest() {
		// For serialization only
	}

	public GetCommentsRequest(String docRefId) {
		this.docRefId = docRefId;
	}

	public String getDocRefId() {
		return docRefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetCommentsResponse();
	}

}
