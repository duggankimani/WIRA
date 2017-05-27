package com.duggan.workflow.shared.requests;

import java.util.Date;

import com.duggan.workflow.shared.responses.GetDashboardDataResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashboardDataRequest extends
		BaseRequest<GetDashboardDataResponse> {

	private String processRefId;
	private Date startDate;
	private Date endDate;
	
	public GetDashboardDataRequest(){
	}
	
	public GetDashboardDataRequest(String processRefId, Date startDate, Date endDate) {
		this.processRefId = processRefId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDashboardDataResponse();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}
}
