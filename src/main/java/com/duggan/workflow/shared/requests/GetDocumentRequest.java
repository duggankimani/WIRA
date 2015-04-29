package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDocumentResult;

public class GetDocumentRequest extends BaseRequest<GetDocumentResult> {

	private Long documentId;
	private Long taskId;
	private boolean isLoadAsAdmin;

	@SuppressWarnings("unused")
	private GetDocumentRequest() {
		// For serialization only
	}

	public GetDocumentRequest(Long documentId, Long taskId) {
		this.documentId = documentId;
		this.taskId = taskId;
	}
	
	public GetDocumentRequest(Long documentId, Long taskId, boolean isLoadAsAdmin) {
		this(documentId, taskId);
		this.isLoadAsAdmin = isLoadAsAdmin;
	}
	
	public BaseResponse createDefaultActionResponse() {
		return new GetDocumentResult();
	}

	public Long getDocumentId() {
		return documentId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public boolean isLoadAsAdmin() {
		return isLoadAsAdmin;
	};
}