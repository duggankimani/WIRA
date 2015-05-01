package com.duggan.workflow.shared.requests;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.responses.BaseResponse;

public class InsertDataRequest extends BaseRequest<BaseResponse> {

	private Catalog catalog;
	private List<DocumentLine> lines = new ArrayList<DocumentLine>();

	@SuppressWarnings("unused")
	private InsertDataRequest() {
	}

	public InsertDataRequest(Catalog catalog) {
		this.catalog = catalog;
	}
	
	public InsertDataRequest(Catalog catalog, List<DocumentLine> lines) {
		this.catalog = catalog;
		this.lines = lines;
	}

	public Catalog getCatalog() {
		return catalog;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new BaseResponse();
	}

	public List<DocumentLine> getLines() {
		return lines;
	}
}
