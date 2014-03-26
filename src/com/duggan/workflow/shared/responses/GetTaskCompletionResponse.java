package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.dashboard.Data;

public class GetTaskCompletionResponse extends BaseResponse {

	private List<Data> data;

	public GetTaskCompletionResponse() {
	}


	public List<Data> getData() {
		return data;
	}


	public void setData(List<Data> data) {
		this.data = data;
	}
}
