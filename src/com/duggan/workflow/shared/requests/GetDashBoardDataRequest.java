package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDashBoardDataResponse;

public class GetDashBoardDataRequest extends
		BaseRequest<GetDashBoardDataResponse> {

	public GetDashBoardDataRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDashBoardDataResponse();
	}
}
