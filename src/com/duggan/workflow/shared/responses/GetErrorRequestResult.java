package com.duggan.workflow.shared.responses;

import java.lang.String;
import java.util.Date;

public class GetErrorRequestResult extends BaseResponse {

	private Date errorDate;
	private String message;
	private String stack;
	private String agent;
	private String remoteAddress;

	@SuppressWarnings("unused")
	public GetErrorRequestResult() {
		// For serialization only
	}

	public GetErrorRequestResult(String message, String stack) {
		this.message = message;
		this.stack = stack;
	}

	public String getMessage() {
		return message;
	}

	public String getStack() {
		return stack;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public Date getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
}
