package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.form.Form;

public class GetFormsResponse extends BaseResponse {

	private List<Form> forms;

	public GetFormsResponse() {
	}

	public GetFormsResponse(List<Form> forms) {
		this.forms = forms;
	}

	public List<Form> getForms() {
		return forms;
	}

	public void setForms(List<Form> forms) {
		this.forms = forms;
	}
}
