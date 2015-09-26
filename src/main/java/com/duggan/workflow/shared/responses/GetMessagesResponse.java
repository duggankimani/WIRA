package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.RequestInfoDto;

public class GetMessagesResponse extends BaseListResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<RequestInfoDto> requests = new ArrayList<RequestInfoDto>();
	
	public GetMessagesResponse() {
	}

	public List<RequestInfoDto> getRequests() {
		return requests;
	}

	public void setRequests(List<RequestInfoDto> requests) {
		this.requests = requests;
	}
	
}
