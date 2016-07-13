package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Attachment;
import com.wira.commons.shared.response.BaseResponse;

public class GetFileTreeResponse extends BaseResponse {

	private ArrayList<Attachment> attachments;

	public GetFileTreeResponse() {
	}

	public GetFileTreeResponse(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}

	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}
}
