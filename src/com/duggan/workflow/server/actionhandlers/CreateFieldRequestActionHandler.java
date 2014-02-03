package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.requests.CreateFieldRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.CreateFieldResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreateFieldRequestActionHandler extends
		BaseActionHandler<CreateFieldRequest, CreateFieldResponse> {

	@Inject
	public CreateFieldRequestActionHandler() {
	}

	@Override
	public void execute(CreateFieldRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Field field = action.getField();
		
		Field rtnfield = FormDaoHelper.createField(field);
		
		CreateFieldResponse response = (CreateFieldResponse)actionResult;
		
		response.setField(rtnfield);
	}

	@Override
	public Class<CreateFieldRequest> getActionType() {
		return CreateFieldRequest.class;
	}
}
