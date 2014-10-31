package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;

public class GetFormsRequest extends BaseRequest<GetFormsResponse> {

	private Long processDefId;
	
	@SuppressWarnings("unused")
	private GetFormsRequest() {
	}
	
	public GetFormsRequest(Long processDefId) {
		this.processDefId = processDefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetFormsResponse();
	}

	public Long getProcessDefId() {
		return processDefId;
	}
}
