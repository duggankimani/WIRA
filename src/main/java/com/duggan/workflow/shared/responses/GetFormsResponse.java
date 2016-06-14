package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.form.Form;

public class GetFormsResponse extends BaseResponse {

	private ArrayList<Form> forms;

	public GetFormsResponse() {
	}

	public GetFormsResponse(ArrayList<Form> forms) {
		this.forms = forms;
	}

	public ArrayList<Form> getForms() {
		return forms;
	}

	public void setForms(ArrayList<Form> forms) {
		this.forms = forms;
	}
}
