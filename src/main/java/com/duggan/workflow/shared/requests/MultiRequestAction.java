package com.duggan.workflow.shared.requests;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class MultiRequestAction extends BaseRequest<MultiRequestActionResult> {

	private ArrayList<BaseRequest<BaseResponse>> requests = new ArrayList<BaseRequest<BaseResponse>>();

	public MultiRequestAction() {
		// For serialization only
	}

	public MultiRequestAction(ArrayList<BaseRequest<BaseResponse>> requests) {
		this.requests = requests;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new MultiRequestActionResult();
	}
	
	public ArrayList<BaseRequest<BaseResponse>> getRequest() {
		return requests;
	}
	
	
	public void addRequest(BaseRequest request){
		request.setRequestCode(requestcode);
		requests.add(request);
	}
	
}
