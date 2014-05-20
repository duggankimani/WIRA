package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

public class MultiRequestActionResult extends BaseResponse {

	private List<BaseResponse> responses = new ArrayList<BaseResponse>();

	@SuppressWarnings("unused")
	public MultiRequestActionResult() {
	}

	public MultiRequestActionResult(List<BaseResponse> responses) {
		this.responses = responses;
	}

	public List<BaseResponse> getReponses() {
		return responses;
	}
	
	public void addResponse(BaseResponse response){
		responses.add(response);
	}
	
	public BaseResponse get(int idx){
		return responses.get(idx);
	}
}
