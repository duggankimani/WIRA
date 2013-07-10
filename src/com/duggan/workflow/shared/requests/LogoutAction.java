package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.LogoutActionResult;

public class LogoutAction extends BaseRequest<LogoutActionResult> {

	public LogoutAction() {
	}
	
	@Override
	public BaseResult createDefaultActionResponse() {
	
		return new LogoutActionResult();
	}
}
