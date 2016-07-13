package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.duggan.workflow.shared.responses.GetProcessLogResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessLogRequest extends BaseRequest<GetProcessLogResponse> {

	private Long processInstanceId;
	
	@SuppressWarnings("unused")
	private GetProcessLogRequest() {
	}
	
	public GetProcessLogRequest(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetProcessLogResponse();
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	
}
