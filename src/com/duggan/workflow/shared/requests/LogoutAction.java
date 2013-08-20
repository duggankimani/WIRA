package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.LogoutActionResult;

public class LogoutAction extends BaseRequest<LogoutActionResult> {

	public LogoutAction() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new LogoutActionResult();
	}
}
