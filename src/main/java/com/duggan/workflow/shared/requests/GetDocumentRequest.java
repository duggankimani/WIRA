package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetDocumentRequest extends BaseRequest<GetDocumentResult> {

	private String docRefId;
	private Long documentId;
	private Long taskId;
	private boolean isLoadAsAdmin;

	@SuppressWarnings("unused")
	private GetDocumentRequest() {
		// For serialization only
	}

	public GetDocumentRequest(String docRefId, Long taskId) {
		this.docRefId = docRefId;
		this.taskId = taskId;
	}
	
	public GetDocumentRequest(String docRefId, Long taskId, boolean isLoadAsAdmin) {
		this(docRefId, taskId);
		this.isLoadAsAdmin = isLoadAsAdmin;
	}
	
	@Deprecated
	public GetDocumentRequest(Long documentId, Long taskId) {
		this.documentId = documentId;
		this.taskId = taskId;
	}
	
	@Deprecated
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
	}

	public String getDocRefId() {
		return docRefId;
	};
}