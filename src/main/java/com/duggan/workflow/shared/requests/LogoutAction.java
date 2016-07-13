package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.LogoutActionResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class LogoutAction extends BaseRequest<LogoutActionResult> {

	public LogoutAction() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new LogoutActionResult();
	}
}
