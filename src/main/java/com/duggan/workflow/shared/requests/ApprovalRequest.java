package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.BaseResponse;

public class ApprovalRequest extends BaseRequest<ApprovalRequestResult> {

	private String username;
	private Document document;

	@SuppressWarnings("unused")
	private ApprovalRequest() {
	}

	public ApprovalRequest(String username, Document doc) {
		this.username = username;
		this.document = doc;
	}

	public String getUsername() {
		return username;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new ApprovalRequestResult(false);
	}

	public Document getDocument() {
		return document;
	}
	
}
