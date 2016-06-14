package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.OutputDocument;

public class GetOutputDocumentsResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<OutputDocument> documents = new ArrayList<OutputDocument>();
	
	public GetOutputDocumentsResponse() {
	}

	public ArrayList<OutputDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<OutputDocument> documents) {
		this.documents = documents;
	}
	
	public void addDocument(OutputDocument doc){
		documents.add(doc);
	}
}
