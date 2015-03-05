package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteAttachmentResponse;

public class DeleteAttachmentRequest extends
		BaseRequest<DeleteAttachmentResponse> {

	private Long attachmentId;
	private Long[] attachmentIds;

	@SuppressWarnings("unused")
	private DeleteAttachmentRequest() {
		// For serialization only
	}

	public DeleteAttachmentRequest(Long attachmentId) {
		this.attachmentId = attachmentId;
	}
	
	public DeleteAttachmentRequest(Long [] attachmentIds){
		this.attachmentIds = attachmentIds;
		
	}

	public Long getAttachmentId() {
		return attachmentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new DeleteAttachmentResponse();
	}

	public Long[] getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(Long[] attachmentIds) {
		this.attachmentIds = attachmentIds;
	}
}
