package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;

import java.lang.Long;

public class GetProcessRequest extends BaseRequest<GetProcessResponse> {

	private Long processDefId;

	@SuppressWarnings("unused")
	private GetProcessRequest() {
		// For serialization only
	}

	public GetProcessRequest(Long processDefId) {
		this.processDefId = processDefId;
	}

	public Long getProcessDefId() {
		return processDefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessResponse();
	}
}
