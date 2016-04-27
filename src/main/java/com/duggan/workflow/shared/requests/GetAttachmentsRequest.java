package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.TreeType;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;

public class GetAttachmentsRequest extends
		BaseRequest<GetAttachmentsResponse> {

	private Long documentId;
	private String userId;
	private String docRefId;
	private TreeType type;
	private String refId;
	private String searchTerm;

	public GetAttachmentsRequest() {
		// For serialization only
	}
	
	public GetAttachmentsRequest with(String userId){
		this.userId=userId;
		return this;
	}

	public GetAttachmentsRequest(Long documentId) {
		this.documentId = documentId;
	}
	
	public GetAttachmentsRequest(String docRefId){
		this.docRefId = docRefId;
	}
	
	public GetAttachmentsRequest(TreeType type, String refId) {
		this.type = type;
		this.refId = refId;
		
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetAttachmentsResponse();
	}

	public String getUserId() {
		return userId;
	}

	public String getDocRefId() {
		return docRefId;
	}

	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public TreeType getType() {
		return type;
	}

	public String getRefId() {
		return refId;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
}
