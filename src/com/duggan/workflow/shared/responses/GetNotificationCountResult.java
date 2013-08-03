package com.duggan.workflow.shared.responses;

public class GetNotificationCountResult extends BaseResult {

	private Integer count;

	public GetNotificationCountResult() {
		// For serialization only
	}

	public GetNotificationCountResult(Integer count) {
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
