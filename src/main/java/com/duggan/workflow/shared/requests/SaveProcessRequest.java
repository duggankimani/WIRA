package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveProcessRequest extends BaseRequest<SaveProcessResponse> {

	private ProcessDef processDef;

	@SuppressWarnings("unused")
	private SaveProcessRequest() {
	}

	public SaveProcessRequest(ProcessDef processDef) {
		this.processDef = processDef;
	}

	public ProcessDef getProcessDef() {
		return processDef;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new SaveProcessResponse();
	}
}
