package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetTaskNodesResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetItemRequest extends BaseRequest<GetTaskNodesResponse> {

	private String userId;
	private Long itemId;
	private boolean isTask;

	public GetItemRequest() {
	}
	
	public GetItemRequest(String userId, Long itemId, boolean isTask) {
		this.userId = userId;
		this.itemId = itemId;
		this.isTask = isTask;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskNodesResponse();
	}

	public boolean isTask() {
		return isTask;
	}

	public void setTask(boolean isTask) {
		this.isTask = isTask;
	}

	public Long getItemId() {
		return itemId;
	}
}
