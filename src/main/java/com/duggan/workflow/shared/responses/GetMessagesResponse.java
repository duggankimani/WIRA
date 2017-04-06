package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.RequestInfoDto;

public class GetMessagesResponse extends BaseListResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ArrayList<RequestInfoDto> requests = new ArrayList<RequestInfoDto>();
	
	public GetMessagesResponse() {
	}

	public ArrayList<RequestInfoDto> getRequests() {
		return requests;
	}

	public void setRequests(ArrayList<RequestInfoDto> requests) {
		this.requests = requests;
	}
	
}
