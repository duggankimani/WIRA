package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.shared.model.TaskType;
import com.duggan.workflow.shared.responses.GetAlertCountResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetAlertCount extends BaseRequest<GetAlertCountResult> {

	private String processRefId;

	public GetAlertCount() {
	}
	
	public GetAlertCount(String processRefId) {
		this.processRefId = processRefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetAlertCountResult(new HashMap<TaskType, Integer>());
	}

	public String getProcessRefId() {
		return processRefId;
	}


}
