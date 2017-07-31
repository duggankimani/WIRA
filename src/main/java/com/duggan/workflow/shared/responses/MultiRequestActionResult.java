package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.wira.commons.shared.response.BaseResponse;

public class MultiRequestActionResult extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<BaseResponse> responses = new ArrayList<BaseResponse>();

	public MultiRequestActionResult() {
	}

	public MultiRequestActionResult(ArrayList<BaseResponse> responses) {
		this.responses = responses;
	}

	public ArrayList<BaseResponse> getReponses() {
		return responses;
	}
	
	public void addResponse(BaseResponse response){
		responses.add(response);
	}
	
	public BaseResponse get(int idx){
		return responses.get(idx);
	}
}
