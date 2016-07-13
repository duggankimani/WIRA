package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.Data;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashBoardDataResponse extends BaseResponse {

	private Integer activeCount;
	private Integer requestCount;
	private Integer failureCount;
	private ArrayList<Data> requestAging;
	private ArrayList<Data> documentCounts;

	public GetDashBoardDataResponse() {
	}

	public GetDashBoardDataResponse(Integer activeCount, Integer requestCount,
			Integer failureCount, ArrayList<Data> requestAging, ArrayList<Data> documentCounts) {
		this.activeCount = activeCount;
		this.requestCount = requestCount;
		this.failureCount = failureCount;
		this.requestAging = requestAging;
		this.documentCounts = documentCounts;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public Integer getRequestCount() {
		return requestCount;
	}

	public Integer getFailureCount() {
		return failureCount;
	}

	public ArrayList<Data> getRequestAging() {
		return requestAging;
	}

	public ArrayList<Data> getDocumentCounts() {
		return documentCounts;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public void setRequestCount(Integer requestCount) {
		this.requestCount = requestCount;
	}

	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}

	public void setRequestAging(ArrayList<Data> requestAging) {
		this.requestAging = requestAging;
	}

	public void setDocumentCounts(ArrayList<Data> documentCounts) {
		this.documentCounts = documentCounts;
	}
}
