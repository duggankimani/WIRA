package com.duggan.workflow.shared.requests;

import java.util.Date;

import com.duggan.workflow.shared.responses.GetDashboardProcessTrendsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashboardProcessTrendsRequest extends
		BaseRequest<GetDashboardProcessTrendsResponse> {

	private String processRefId;
	private Date startDate;
	private Date endDate;
	private String period;
	
	public GetDashboardProcessTrendsRequest(){
	}
	
	public GetDashboardProcessTrendsRequest(String processRefId, Date startDate, Date endDate,String period) {
		this.processRefId = processRefId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.period = period;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDashboardProcessTrendsResponse();
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
}
