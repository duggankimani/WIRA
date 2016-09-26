package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.requests.GetRecentTasksRequest;
import com.duggan.workflow.shared.responses.GetRecentTasksResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

/**
 * 
 * @author duggan
 * 
 */
public class GetRecentTasksActionHandler extends
		AbstractActionHandler<GetRecentTasksRequest, GetRecentTasksResult> {

	@Inject
	public GetRecentTasksActionHandler() {
	}

	@Override
	public void execute(GetRecentTasksRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		String userId = action.getUserId()==null? SessionHelper.getCurrentUser().getUserId():
			action.getUserId();
				
		List<Doc> recentTasks = DocumentDaoHelper.getRecentTasks(action.getProcessRefId(), userId,action.getOffset(), action.getLength());
		GetRecentTasksResult result = (GetRecentTasksResult) actionResult;
		result.setTasks((ArrayList<Doc>) recentTasks);

	}

	@Override
	public void undo(GetRecentTasksRequest action, GetRecentTasksResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetRecentTasksRequest> getActionType() {
		return GetRecentTasksRequest.class;
	}
}
