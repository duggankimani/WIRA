package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.Attachment;

public class GetFileTreeResponse extends BaseResponse {

	private List<Attachment> attachments;

	public GetFileTreeResponse() {
	}

	public GetFileTreeResponse(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
}
