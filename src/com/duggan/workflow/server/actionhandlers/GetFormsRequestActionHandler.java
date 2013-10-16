package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.helper.dao.FormDaoHelper;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetFormsRequestActionHandler extends
		BaseActionHandler<GetFormsRequest, GetFormsResponse> {

	@Inject
	public GetFormsRequestActionHandler() {
	}

	@Override
	public void execute(GetFormsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		
		List<Form> forms = FormDaoHelper.getForms();
		
		GetFormsResponse response = (GetFormsResponse)actionResult;
		
		response.setForms(forms);
		
	}

	@Override
	public Class<GetFormsRequest> getActionType() {
		return GetFormsRequest.class;
	}
}
