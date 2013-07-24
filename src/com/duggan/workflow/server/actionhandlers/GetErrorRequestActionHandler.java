package com.duggan.workflow.server.actionhandlers;

import java.util.Date;

import com.duggan.workflow.server.dao.model.ErrorLog;
import com.duggan.workflow.server.helper.error.ErrorLogDaoHelper;
import com.duggan.workflow.shared.requests.GetErrorRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetErrorRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetErrorRequestActionHandler extends
		BaseActionHandler<GetErrorRequest, GetErrorRequestResult> {

	@Inject
	public GetErrorRequestActionHandler() {
	}

	@Override
	public GetErrorRequestResult execute(GetErrorRequest action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		
		ErrorLog log = ErrorLogDaoHelper.retrieveError(action.getErrorId());
		
		GetErrorRequestResult result = (GetErrorRequestResult)actionResult;
		
		if(log!=null){
			result.setMessage(log.getMsg());
			result.setStack(log.getStackTrace());
			result.setErrorDate(log.getCreated());
			result.setAgent(log.getAgent());
			result.setRemoteAddress(log.getRemoteAddress());
		}else{
			result.setMessage("No Error Log for Error no: "+action.getErrorId());
			result.setStack("");
			result.setErrorDate(new Date());
		}
		result.setErrorCode(0);
		return result;
	}
	
	@Override
	public Class<GetErrorRequest> getActionType() {
		return GetErrorRequest.class;
	}
}
