package com.duggan.workflow.shared.requests;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.responses.GenerateFilePathResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GenerateFilePathRequest extends BaseRequest<GenerateFilePathResponse> {

	private Doc doc;
	private String fieldRefId;
	private ArrayList<String> fileFieldNames;

	@SuppressWarnings("unused")
	private GenerateFilePathRequest() {
		// For serialization only
	}
	
	public GenerateFilePathRequest(Doc doc, String fieldRefId, ArrayList<String> fileFieldNames) {
		// For serialization only
		this.doc = doc;
		this.fieldRefId = fieldRefId;
		this.fileFieldNames = fileFieldNames;
		
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GenerateFilePathResponse();
	}

	public Doc getDoc() {
		return doc;
	}

	public String getFieldRefId() {
		return fieldRefId;
	}

	public ArrayList<String> getFileFieldNames() {
		return fileFieldNames;
	}

}
