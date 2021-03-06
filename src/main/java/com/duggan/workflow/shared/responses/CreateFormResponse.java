package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.form.Form;
import com.wira.commons.shared.response.BaseResponse;

public class CreateFormResponse extends BaseResponse {

	private Form form;

	public CreateFormResponse() {
	}

	public CreateFormResponse(Form form) {
		this.form = form;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
