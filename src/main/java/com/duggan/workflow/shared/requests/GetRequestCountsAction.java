package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.shared.responses.GetRequestCountsResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

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
