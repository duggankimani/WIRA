package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetProcessImportStatusResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessImportStatus extends BaseRequest<GetProcessImportStatusResponse> {

	public GetProcessImportStatus() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessImportStatusResponse();
	}
}
