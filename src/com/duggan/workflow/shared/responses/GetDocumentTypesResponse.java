package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.DocumentType;

public class GetDocumentTypesResponse extends BaseResponse {

	private List<DocumentType> documentTypes;

	public GetDocumentTypesResponse() {
	}

	public GetDocumentTypesResponse(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}
}
