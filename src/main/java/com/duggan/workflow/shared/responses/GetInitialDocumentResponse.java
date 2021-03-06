package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.wira.commons.shared.response.BaseResponse;

public class GetInitialDocumentResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Doc doc;
	private ArrayList<TaskStepDTO> steps;
	private ArrayList<String> conditionalFields;

	public GetInitialDocumentResponse() {
	}

	public GetInitialDocumentResponse(Doc doc) {
		this.doc = doc;
	}

	public Doc getDoc() {
		return doc;
	}

	public void setDoc(Doc doc) {
		this.doc = doc;
	}

	public ArrayList<TaskStepDTO> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<TaskStepDTO> steps) {
		this.steps = steps;
	}

	public void setConditionalFields(ArrayList<String> conditionalFields) {
		this.conditionalFields = conditionalFields;
	}

	public ArrayList<String> getConditionalFields() {
		return conditionalFields;
	}
}
