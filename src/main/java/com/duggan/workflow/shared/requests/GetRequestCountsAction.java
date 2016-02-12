package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetRequestCountsResult;

public class GetRequestCountsAction extends BaseRequest<GetRequestCountsResult> {

	private String processId;
	private String userId;
	
	@SuppressWarnings("unused")
	private GetRequestCountsAction() {
	}
	
	public GetRequestCountsAction(String processId,String userId){
		this.processId = processId;
		this.userId = userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetRequestCountsResult(new HashMap<String,Integer>());
	}

	public String getUserId() {
		return userId;
	}

	public String getProcessId() {
		return processId;
	}

}
