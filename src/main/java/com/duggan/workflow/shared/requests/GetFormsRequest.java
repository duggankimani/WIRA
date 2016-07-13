package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetFormsRequest extends BaseRequest<GetFormsResponse> {

	private Long processDefId;
	private String processRefId;
	private boolean isLoadFields;
	
	public boolean isLoadFields() {
		return isLoadFields;
	}

	@SuppressWarnings("unused")
	private GetFormsRequest() {
	}
	
	public GetFormsRequest(Long processDefId) {
		this.processDefId = processDefId;
	}
	
	public GetFormsRequest(Long processDefId, boolean isLoadFields) {
		this.processDefId = processDefId;
		this.isLoadFields = isLoadFields;
	}
	
	public GetFormsRequest(String processRefId) {
		this.processRefId = processRefId;
	}
	
	public GetFormsRequest(String processRefId, boolean isLoadFields) {
		this.processRefId = processRefId;
		this.isLoadFields = isLoadFields;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetFormsResponse();
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public String getProcessRefId() {
		return processRefId;
	}
}
