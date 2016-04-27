package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDataResponse;

public class GetDataRequest extends BaseRequest<GetDataResponse> {

	private Long catalogId;
	private String catalogRefId;
	private String searchTerm;
	
	public GetDataRequest() {
	}
	
	public GetDataRequest(Long catalogId) {
		this.catalogId = catalogId;
	}
	
	public GetDataRequest(String catalogRefId){
		this.catalogRefId = catalogRefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDataResponse();
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public String getCatalogRefId() {
		return catalogRefId;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
}
