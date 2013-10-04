package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.form.FormModel;

public class GetFormModelResponse extends BaseResponse {

	private List<FormModel> formModel = new ArrayList<FormModel>();

	public GetFormModelResponse() {
	}

	public void addItem(FormModel model){
		formModel.add(model);
	}
	
	public GetFormModelResponse(List<FormModel> formModel) {
		this.formModel = formModel;
	}

	public List<FormModel> getFormModel() {
		return formModel;
	}
	
	public void setFormModel(List<FormModel> formModel){
		this.formModel= formModel;
	}
}
