package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.ExportFormResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class ExportFormRequest extends BaseRequest<ExportFormResponse> {

	private String formRefId;

	@SuppressWarnings("unused")
	private ExportFormRequest() {
		// For serialization only
	}

	public ExportFormRequest(String formRefId) {
		this.formRefId = formRefId;
	}

	public String getFormRefId() {
		return formRefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		// TODO Auto-generated method stub
		return new ExportFormResponse();
	}
}
