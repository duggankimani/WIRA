package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.form.FormModel;

public class GetFormModelResponse extends BaseResponse {

	private ArrayList<FormModel> formModel = new ArrayList<FormModel>();

	public GetFormModelResponse() {
	}

	public void addItem(FormModel model){
		formModel.add(model);
	}
	
	public GetFormModelResponse(ArrayList<FormModel> formModel) {
		this.formModel = formModel;
	}

	public ArrayList<FormModel> getFormModel() {
		return formModel;
	}
	
	public void setFormModel(ArrayList<FormModel> formModel){
		this.formModel= formModel;
	}
}
