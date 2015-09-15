package com.duggan.workflow.shared.requests;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.responses.BaseResponse;

public class InsertDataRequest extends BaseRequest<BaseResponse> {

	private Long catalogId;
	private List<DocumentLine> lines = new ArrayList<DocumentLine>();

	@SuppressWarnings("unused")
	private InsertDataRequest() {
	}

	public InsertDataRequest(Long catalogId) {
		this.catalogId = catalogId;
		
	}
	
	public InsertDataRequest(Long catalogId, List<DocumentLine> lines) {
		this.catalogId = catalogId;
		this.lines = lines;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new BaseResponse();
	}

	public List<DocumentLine> getLines() {
		return lines;
	}

	public Long getCatalogId() {
		return catalogId;
	}
}
