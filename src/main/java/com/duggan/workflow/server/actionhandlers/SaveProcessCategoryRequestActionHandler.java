package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.requests.SaveProcessCategoryRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveProcessCategoryResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveProcessCategoryRequestActionHandler extends
		BaseActionHandler<SaveProcessCategoryRequest, SaveProcessCategoryResponse> {

	@Inject
	public SaveProcessCategoryRequestActionHandler() {
	}

	@Override
	public void execute(SaveProcessCategoryRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		ProcessCategory category = ProcessDefHelper.save(action.getCategory());
		((SaveProcessCategoryResponse)actionResult).setCategory(category);
	}
	
	@Override
	public Class<SaveProcessCategoryRequest> getActionType() {
		return SaveProcessCategoryRequest.class;
	}
}
