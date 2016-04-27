package com.duggan.workflow.shared.requests;

import java.util.List;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GenerateFilePathResponse;

public class GenerateFilePathRequest extends BaseRequest<GenerateFilePathResponse> {

	private Doc doc;
	private Long fieldId;
	private List<String> fileFieldNames;

	@SuppressWarnings("unused")
	private GenerateFilePathRequest() {
		// For serialization only
	}
	
	public GenerateFilePathRequest(Doc doc, Long fieldId, List<String> fileFieldNames) {
		// For serialization only
		this.doc = doc;
		this.fieldId = fieldId;
		this.fileFieldNames = fileFieldNames;
		
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GenerateFilePathResponse();
	}

	public Doc getDoc() {
		return doc;
	}

	public Long getFieldId() {
		return fieldId;
	}

	public List<String> getFileFieldNames() {
		return fileFieldNames;
	}

}
