package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.Trigger;
import com.duggan.workflow.shared.requests.GetTriggersRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTriggersResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTriggersRequestHandler extends
		AbstractActionHandler<GetTriggersRequest, GetTriggersResponse> {

	@Inject
	public GetTriggersRequestHandler() {
	}

	@Override
	public void execute(GetTriggersRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		List<Trigger> triggers = null;

		if (action.getProcessRefId()!=null) {
			triggers = ProcessDaoHelper.getTriggers(action.getProcessRefId(),action.getSearchTerm());
		} else {
			triggers = ProcessDaoHelper.getTriggers();
		}

		((GetTriggersResponse) actionResult).setTriggers((ArrayList<Trigger>) triggers);
	}

	public java.lang.Class<GetTriggersRequest> getActionType() {
		return GetTriggersRequest.class;
	};
}
