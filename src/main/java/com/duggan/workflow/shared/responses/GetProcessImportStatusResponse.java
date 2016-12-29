package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.wira.commons.shared.response.BaseResponse;

public class GetProcessImportStatusResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> status = new ArrayList<String>();
	public ArrayList<String> getStatus() {
		return status;
	}
	public void setStatus(ArrayList<String> status) {
		this.status = status;
	}
	public GetProcessImportStatusResponse() {
		
	}
}
