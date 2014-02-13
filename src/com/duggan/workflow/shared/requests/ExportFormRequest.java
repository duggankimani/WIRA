package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExportFormResponse;

import java.lang.Long;

public class ExportFormRequest extends BaseRequest<ExportFormResponse> {

	private Long formId;

	@SuppressWarnings("unused")
	private ExportFormRequest() {
		// For serialization only
	}

	public ExportFormRequest(Long formId) {
		this.formId = formId;
	}

	public Long getFormId() {
		return formId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		// TODO Auto-generated method stub
		return new ExportFormResponse();
	}
}
