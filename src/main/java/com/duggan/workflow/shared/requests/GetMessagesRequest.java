package com.duggan.workflow.shared.requests;

import java.util.Date;

import com.duggan.workflow.shared.responses.GetMessagesResponse;
import com.wira.commons.shared.response.BaseResponse;

public class GetMessagesRequest extends BaseListRequest<GetMessagesResponse> {

	private String status;
	private String commandType;
	private Date startDate;
	private Date endDate;

	public GetMessagesRequest() {}
	
	public GetMessagesRequest(String status, String commandType, Date startDate, Date endDate) {
		this.status = status;
		this.commandType = commandType;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetMessagesResponse();
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getCommandType() {
		return commandType;
	}


	public void setCommandType(String commandType) {
		this.commandType = commandType;
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
	
}
