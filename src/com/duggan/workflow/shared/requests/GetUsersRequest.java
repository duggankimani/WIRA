package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;

public class GetUsersRequest extends BaseRequest<GetUsersResponse> {

	public GetUsersRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		// TODO Auto-generated method stub
		return new GetUsersResponse();
	}
}
