package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.responses.CreateFieldResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class CreateFieldRequest extends BaseRequest<CreateFieldResponse> {

	private Field field;

	@SuppressWarnings("unused")
	private CreateFieldRequest() {
		// For serialization only
	}

	public CreateFieldRequest(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new CreateFieldResponse();
	}
}
