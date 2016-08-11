package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.CreateFormRequest;
import com.duggan.workflow.shared.responses.CreateFormResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class CreateFormRequestActionHandler extends
		AbstractActionHandler<CreateFormRequest, CreateFormResponse> {

	@Inject
	public CreateFormRequestActionHandler() {
	}
	
	@Override
	public void execute(CreateFormRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		Form form = action.getForm();
		//form =  FormDaoHelper.createForm(form, true);
		form =  FormDaoHelper.createJson(form);
		
		CreateFormResponse response = (CreateFormResponse)actionResult;
		response.setForm(form);
	}
	
	@Override
	public Class<CreateFormRequest> getActionType() {
		return CreateFormRequest.class;
	}
}
