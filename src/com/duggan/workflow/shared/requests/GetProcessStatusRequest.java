package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessStatusRequestResult;
import java.lang.Long;

public class GetProcessStatusRequest extends
		BaseRequest<GetProcessStatusRequestResult> {

	private Long processInstanceId;

	@SuppressWarnings("unused")
	public GetProcessStatusRequest() {
		// For serialization only
	}

	public GetProcessStatusRequest(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessStatusRequestResult();
	}
}
