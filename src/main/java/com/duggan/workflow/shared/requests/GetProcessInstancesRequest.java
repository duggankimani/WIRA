package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.CaseFilter;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessInstancesResponse;

public class GetProcessInstancesRequest extends BaseRequest<GetProcessInstancesResponse> {

	private CaseFilter filter;
	
	public GetProcessInstancesRequest() {
	}
	
	public GetProcessInstancesRequest(CaseFilter filter) {
		this.filter = filter;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessInstancesResponse();
	}

	public CaseFilter getFilter() {
		return filter;
	}

}
