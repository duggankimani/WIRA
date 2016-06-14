package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.DocumentType;

public class GetDocumentTypesResponse extends BaseResponse {

	private ArrayList<DocumentType> documentTypes;

	public GetDocumentTypesResponse() {
	}

	public GetDocumentTypesResponse(ArrayList<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public ArrayList<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(ArrayList<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}
}
