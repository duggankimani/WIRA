package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetContextRequestResult;

public class GetContextRequest extends BaseRequest<GetContextRequestResult> {

	public GetContextRequest() {
	}
	
	@Override
	public BaseResult createDefaultActionResponse() {
	
		return new GetContextRequestResult();
	}
}
