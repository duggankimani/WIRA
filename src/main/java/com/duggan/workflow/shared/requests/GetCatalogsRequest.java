package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetCatalogsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetCatalogsRequest extends BaseRequest<GetCatalogsResponse> {

	private String catalogRefId;
	private boolean isLoadViews;
	private String searchTerm;
	
	public GetCatalogsRequest() {
	}
	
	public GetCatalogsRequest(String catalogRefId) {
		this.catalogRefId = catalogRefId;
	}
	
	public GetCatalogsRequest(boolean isLoadViews) {
		this.isLoadViews = isLoadViews;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetCatalogsResponse();
	}

	public boolean isLoadViews() {
		return isLoadViews;
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
