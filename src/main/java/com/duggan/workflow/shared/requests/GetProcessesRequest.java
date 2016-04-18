package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;

public class GetProcessesRequest extends BaseRequest<GetProcessesResponse> {

	boolean isLoadDetails;
	private String searchTerm;
	
	private GetProcessesRequest() {
	}
	
	public GetProcessesRequest(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
	public GetProcessesRequest(String searchTerm, boolean isLoadDetails) {
		this.searchTerm = searchTerm;
		this.isLoadDetails = isLoadDetails;
	}
	
	public GetProcessesRequest(boolean isLoadDetails) {
		this.isLoadDetails = isLoadDetails;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessesResponse();
	}

	public boolean isLoadDetails() {
		return isLoadDetails;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
}
