package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;

import java.lang.Long;

public class DeleteProcessRequest extends
		BaseRequest<DeleteProcessResponse> {

	private Long processId;

	@SuppressWarnings("unused")
	private DeleteProcessRequest() {
	}

	public DeleteProcessRequest(Long processId) {
		this.processId = processId;
	}

	public Long getProcessId() {
		return processId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {		
		return new DeleteProcessResponse();
	}
}
