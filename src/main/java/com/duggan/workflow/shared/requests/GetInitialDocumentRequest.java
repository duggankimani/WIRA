package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetInitialDocumentResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

/**
 * This is a loaded request that loads a document, TaskSteps & 
 * executes before trigger
 * <p>
 * @author duggan
 *
 */
public class GetInitialDocumentRequest extends BaseRequest<GetInitialDocumentResponse> {

	private String docRefId;
	private Long documentId;
	private Long taskId;
	private boolean isLoadAsAdmin=false;

	@SuppressWarnings("unused")
	private GetInitialDocumentRequest() {
		// For serialization only
	}
	
	public GetInitialDocumentRequest(String docRefId, Long taskId) {
		this.docRefId = docRefId;
		this.taskId = taskId;
	}
	
	public GetInitialDocumentRequest(String docRefId, Long taskId, boolean isLoadAsAdmin) {
		this(docRefId,taskId);
		this.isLoadAsAdmin=isLoadAsAdmin;
	}

	@Deprecated
	public GetInitialDocumentRequest(Long documentId, Long taskId) {
		this.documentId = documentId;
		this.taskId = taskId;
	}
	
	@Deprecated
	public GetInitialDocumentRequest(Long documentId, Long taskId, boolean isLoadAsAdmin) {
		this(documentId,taskId);
		this.isLoadAsAdmin=isLoadAsAdmin;
	}
	
	public BaseResponse createDefaultActionResponse() {
		return new GetInitialDocumentResponse();
	}
	
	public String getDocRefId() {
		return docRefId;
	}

	@Deprecated
	public Long getDocumentId() {
		return documentId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public boolean isLoadAsAdmin() {
		return isLoadAsAdmin;
	}

	public void setLoadAsAdmin(boolean isLoadAsAdmin) {
		this.isLoadAsAdmin = isLoadAsAdmin;
	}
}