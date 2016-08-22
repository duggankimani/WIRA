package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetFormsRequestActionHandler extends
		AbstractActionHandler<GetFormsRequest, GetFormsResponse> {

	@Inject
	public GetFormsRequestActionHandler() {
	}

	@Override
	public void execute(GetFormsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		List<Form> forms = new ArrayList<Form>();

		if(action.getProcessRefId()!=null){
			forms = FormDaoHelper.getFormsJson(action.getProcessRefId(), false);
		}else if (action.getProcessDefId() != null) {
			forms = FormDaoHelper.getForms(action.getProcessDefId(),
					action.isLoadFields());
		} 

		GetFormsResponse response = (GetFormsResponse) actionResult;

		response.setForms((ArrayList<Form>) forms);

	}

	@Override
	public Class<GetFormsRequest> getActionType() {
		return GetFormsRequest.class;
	}
}
