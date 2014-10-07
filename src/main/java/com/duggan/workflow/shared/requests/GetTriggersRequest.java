package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTriggersResponse;

public class GetTriggersRequest extends BaseRequest<GetTriggersResponse> {


	public GetTriggersRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetTriggersResponse();
	}

}
