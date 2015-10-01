package com.duggan.workflow.server.actionhandlers;

import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.entities.RequestInfo;
import org.jbpm.executor.impl.ExecutorFactory;

import com.duggan.workflow.shared.model.RequestInfoDto;
import com.duggan.workflow.shared.requests.SendMessageRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SendMessageResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Create Local Documents
 * 
 * @author duggan
 *
 */
public class SendMessageActionHandler extends
		BaseActionHandler<SendMessageRequest, SendMessageResponse> {

	@Inject
	public SendMessageActionHandler() {
	}
	
	@Override
	public void execute(SendMessageRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		RequestInfoDto dto = action.getDto();
		ExecutorFactory.getExecutor().updateRequest(dto, true);
		RequestInfo info = ExecutorModule.getInstance().getExecutorServiceEntryPoint().getRequestById(dto.getRefId());
	}
	
	@Override
	public Class<SendMessageRequest> getActionType() {
		return SendMessageRequest.class;
	}

}
