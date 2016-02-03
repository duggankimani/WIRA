package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAllOrganizationsResponse;

public class GetAllOganizationsRequest extends BaseRequest<GetAllOrganizationsResponse> {


	@SuppressWarnings("unused")
	private GetAllOganizationsRequest() {
		// For serialization only
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetAllOrganizationsResponse();
	}
}
