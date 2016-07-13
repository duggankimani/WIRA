package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.DocumentType;
import com.wira.commons.shared.response.BaseResponse;

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
