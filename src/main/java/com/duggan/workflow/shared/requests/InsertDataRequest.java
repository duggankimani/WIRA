package com.duggan.workflow.shared.requests;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.DocumentLine;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class InsertDataRequest extends BaseRequest<BaseResponse> {

	private Long catalogId;
	private ArrayList<DocumentLine> lines = new ArrayList<DocumentLine>();

	@SuppressWarnings("unused")
	private InsertDataRequest() {
	}

	public InsertDataRequest(Long catalogId) {
		this.catalogId = catalogId;
		
	}
	
	public InsertDataRequest(Long catalogId, ArrayList<DocumentLine> lines) {
		this.catalogId = catalogId;
		this.lines = lines;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new BaseResponse();
	}

	public ArrayList<DocumentLine> getLines() {
		return lines;
	}

	public Long getCatalogId() {
		return catalogId;
	}
}
