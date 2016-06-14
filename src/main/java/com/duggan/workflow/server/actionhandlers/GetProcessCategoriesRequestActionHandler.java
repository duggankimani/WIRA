package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.requests.GetProcessCategoriesRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProcessCategoriesRequestActionHandler extends
		AbstractActionHandler<GetProcessCategoriesRequest, GetProcessCategoriesResponse> {

	@Inject
	public GetProcessCategoriesRequestActionHandler() {
	}

	@Override
	public void execute(GetProcessCategoriesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetProcessCategoriesResponse response = (GetProcessCategoriesResponse)actionResult;
		response.setCategories((ArrayList<ProcessCategory>) ProcessDefHelper.getProcessCategories());
	}
	
	@Override
	public Class<GetProcessCategoriesRequest> getActionType() {
		return GetProcessCategoriesRequest.class;
	}
}
