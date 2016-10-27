package com.duggan.workflow.shared.requests;

import javax.jws.soap.SOAPBinding.Use;

import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class CreateDocumentRequest extends
		BaseRequest<CreateDocumentResult> {

	private Document document;
	private String processRefId;

	@SuppressWarnings("unused")
	private CreateDocumentRequest() {
		
	}
	
	/**
	 * Create document request in process processRefId
	 * 
	 * @param processRefId
	 */
	public CreateDocumentRequest(String processRefId){
		this.processRefId = processRefId;
	}
	
	/**
	 * Please use {@link #CreateDocumentRequest(String)} 
	 * @param document
	 */
	public CreateDocumentRequest(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new CreateDocumentResult();
	}
	
	public String getProcessRefId() {
		return processRefId;
	}

}
