package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.requests.SaveProcessCategoryRequest;
import com.duggan.workflow.shared.responses.SaveProcessCategoryResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class SaveProcessCategoryRequestActionHandler extends
		AbstractActionHandler<SaveProcessCategoryRequest, SaveProcessCategoryResponse> {

	@Inject
	public SaveProcessCategoryRequestActionHandler() {
	}

	@Override
	public void execute(SaveProcessCategoryRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		ProcessCategory category = ProcessDaoHelper.save(action.getCategory());
		((SaveProcessCategoryResponse)actionResult).setCategory(category);
	}
	
	@Override
	public Class<SaveProcessCategoryRequest> getActionType() {
		return SaveProcessCategoryRequest.class;
	}
}
