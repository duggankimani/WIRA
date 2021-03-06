package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.responses.DeleteFormModelResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteFormModelRequest extends
		BaseRequest<DeleteFormModelResponse> {

	private FormModel model;

	public DeleteFormModelRequest() {
		// For serialization only
	}

	public DeleteFormModelRequest(FormModel model) {
		this.model = model;
	}

	public FormModel getModel() {
		return model;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new DeleteFormModelResponse();
	}
}
