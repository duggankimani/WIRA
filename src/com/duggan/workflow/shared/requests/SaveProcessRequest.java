package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.duggan.workflow.shared.model.ProcessDef;

public class SaveProcessRequest extends BaseRequest<SaveProcessResponse> {

	private ProcessDef processDef;

	@SuppressWarnings("unused")
	private SaveProcessRequest() {
		//for serialization purposes
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
