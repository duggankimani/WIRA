package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.form.Field;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteTableRowResponse extends BaseResponse {

	private Field field;

	public DeleteTableRowResponse() {
		// For serialization only
	}

	public DeleteTableRowResponse(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}
