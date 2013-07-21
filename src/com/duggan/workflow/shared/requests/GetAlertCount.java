package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.requests.GetAlertCountResult;
import com.duggan.workflow.shared.responses.BaseResult;

public class GetAlertCount extends BaseRequest<GetAlertCountResult> {

	public GetAlertCount() {
	}
	
	@Override
	public BaseResult createDefaultActionResponse() {
	
		return new GetAlertCountResult(new HashMap<TaskType, Integer>());
	}
}
