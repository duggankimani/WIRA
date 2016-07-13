package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetContextRequest extends BaseRequest<GetContextRequestResult> {

	public GetContextRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetContextRequestResult();
	}
}
