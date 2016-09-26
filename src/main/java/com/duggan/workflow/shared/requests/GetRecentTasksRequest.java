package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetRecentTasksResult;
import com.wira.commons.shared.response.BaseResponse;

public class GetRecentTasksRequest extends BaseListRequest<GetRecentTasksResult> {

	private String processRefId;
	private String userId;
	
	@SuppressWarnings("unused")
	private GetRecentTasksRequest() {
	}
	
	public GetRecentTasksRequest(String processRefId, String userId, int offset, int limit){
		this.processRefId = processRefId;
		this.userId = userId;
		setOffset(offset);
		setLength(limit);
	}

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetRecentTasksResult();
	}


}
