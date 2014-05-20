package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.form.Field;

public class CreateFieldResponse extends BaseResponse {

	private Field field;

	public CreateFieldResponse() {
		// For serialization only
	}

	public CreateFieldResponse(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}
