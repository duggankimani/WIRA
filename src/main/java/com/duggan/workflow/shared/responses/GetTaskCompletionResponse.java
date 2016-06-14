package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.Data;

public class GetTaskCompletionResponse extends BaseResponse {

	private ArrayList<Data> data;

	public GetTaskCompletionResponse() {
	}


	public ArrayList<Data> getData() {
		return data;
	}


	public void setData(ArrayList<Data> data) {
		this.data = data;
	}
}
