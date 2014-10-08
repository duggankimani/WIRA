package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetInitialDocumentResponse;

/**
 * This is a loaded request that loads a document, TaskSteps & 
 * executes before trigger
 * <p>
 * @author duggan
 *
 */
public class GetInitialDocumentRequest extends BaseRequest<GetInitialDocumentResponse> {

	private Long documentId;
	private Long taskId;

	@SuppressWarnings("unused")
	private GetInitialDocumentRequest() {
		// For serialization only
	}

	public GetInitialDocumentRequest(Long documentId, Long taskId) {
		this.documentId = documentId;
		this.taskId = taskId;
	}
	
	public BaseResponse createDefaultActionResponse() {
		return new GetInitialDocumentResponse();
	}

	public Long getDocumentId() {
		return documentId;
	}

	public Long getTaskId() {
		return taskId;
	};
}