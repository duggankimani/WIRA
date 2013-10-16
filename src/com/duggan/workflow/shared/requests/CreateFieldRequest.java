package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.CreateFieldResponse;
import com.duggan.workflow.shared.model.form.Field;

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
