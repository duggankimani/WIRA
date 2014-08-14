package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.OutputDocument;

public class GetOutputDocumentsResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<OutputDocument> documents = new ArrayList<OutputDocument>();
	
	public GetOutputDocumentsResponse() {
	}

	public List<OutputDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<OutputDocument> documents) {
		this.documents = documents;
	}
	
	public void addDocument(OutputDocument doc){
		documents.add(doc);
	}
}
