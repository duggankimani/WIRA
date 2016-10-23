package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetProcessSchemaResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessSchemaRequest extends BaseRequest<GetProcessSchemaResponse> {

	private String processRefId;

	@SuppressWarnings("unused")
	private GetProcessSchemaRequest() {
		// For serialization only
	}
	
	public GetProcessSchemaRequest(String processRefId) {
		this.processRefId = processRefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessSchemaResponse();
	}

	public String getProcessRefId() {
		return processRefId;
	}

}
