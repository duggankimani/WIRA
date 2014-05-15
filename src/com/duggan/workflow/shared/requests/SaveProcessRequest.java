package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveProcessResponse;

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
