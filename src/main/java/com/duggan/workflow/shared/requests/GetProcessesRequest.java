package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;

public class GetProcessesRequest extends BaseRequest<GetProcessesResponse> {

	boolean isLoadDetails;
	
	private GetProcessesRequest() {
	}
	
	public GetProcessesRequest(boolean isLoadDetails) {
		this.isLoadDetails = isLoadDetails;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessesResponse();
	}

	public boolean isLoadDetails() {
		return isLoadDetails;
	}
}
