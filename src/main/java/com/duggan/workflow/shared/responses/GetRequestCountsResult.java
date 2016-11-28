package com.duggan.workflow.shared.responses;

import java.util.HashMap;

public class GetRequestCountsResult extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Integer> counts;

	@SuppressWarnings("unused")
	private GetRequestCountsResult() {
	}

	public GetRequestCountsResult(HashMap<String, Integer> counts) {
		this.counts = counts;
	}

	public HashMap<String, Integer> getCounts() {
		return counts;
	}
}
