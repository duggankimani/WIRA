package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetOrgsResponse;

public class GetOrgsRequest extends BaseListRequest<GetOrgsResponse> {
	
	private String searchText;
	
	public GetOrgsRequest() {
	}
	
	public GetOrgsRequest(String searchText, int offset, int length){
		this.searchText = searchText;
		setOffset(offset);
		setLength(length);
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetOrgsResponse();
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
}
