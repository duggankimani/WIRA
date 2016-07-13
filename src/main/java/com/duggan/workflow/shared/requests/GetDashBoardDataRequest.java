package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetDashBoardDataResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashBoardDataRequest extends
		BaseRequest<GetDashBoardDataResponse> {

	public GetDashBoardDataRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDashBoardDataResponse();
	}
}
