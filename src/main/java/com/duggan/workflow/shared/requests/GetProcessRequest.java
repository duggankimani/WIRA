package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;

public class GetProcessRequest extends BaseRequest<GetProcessResponse> {

	private Long processDefId;
	private String processRefId;

	@SuppressWarnings("unused")
	private GetProcessRequest() {
		// For serialization only
	}

	public GetProcessRequest(Long processDefId) {
		this.processDefId = processDefId;
	}
	
	public GetProcessRequest(String processRefId) {
		this.processRefId = processRefId;
		
	}

	public Long getProcessDefId() {
		return processDefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessResponse();
	}

	public String getProcessRefId() {
		return processRefId;
	}

}
