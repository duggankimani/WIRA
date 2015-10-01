package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.shared.model.RequestInfoDto;
import com.duggan.workflow.shared.requests.GetMessagesRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetMessagesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetMessagesActionHandler extends
		BaseActionHandler<GetMessagesRequest, GetMessagesResponse> {

	@Inject
	public GetMessagesActionHandler() {
	}

	@Override
	public void execute(GetMessagesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		List<RequestInfo> requestInfo = ExecutorModule.getInstance()
				.getExecutorServiceEntryPoint()
				.getAllRequests(action.getOffset(), action.getLength());

		List<RequestInfoDto> dtos = new ArrayList<RequestInfoDto>();
		for (RequestInfo info : requestInfo) {
			RequestInfoDto dto = info.toDto();
			dtos.add(dto);

		}
		((GetMessagesResponse) actionResult).setRequests(dtos);
		((GetMessagesResponse) actionResult).setTotalCount(ExecutorModule
				.getInstance().getExecutorServiceEntryPoint()
				.getAllRequestCount());
	}

	@Override
	public Class<GetMessagesRequest> getActionType() {
		return GetMessagesRequest.class;
	}

}
