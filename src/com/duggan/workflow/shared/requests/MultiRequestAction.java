package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;

import java.util.ArrayList;
import java.util.List;

public class MultiRequestAction extends BaseRequest<MultiRequestActionResult> {

	private List<BaseRequest<BaseResponse>> requests = new ArrayList<BaseRequest<BaseResponse>>();

	public MultiRequestAction() {
		// For serialization only
	}

	public MultiRequestAction(List<BaseRequest<BaseResponse>> requests) {
		this.requests = requests;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new MultiRequestActionResult();
	}
	
	public List<BaseRequest<BaseResponse>> getRequest() {
		return requests;
	}
	
	
	public void addRequest(BaseRequest request){
		request.setRequestCode(requestcode);
		requests.add(request);
	}
	
}
