package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;

public class GetFormsRequest extends BaseRequest<GetFormsResponse> {

	private Long processDefId;
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
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetFormsResponse();
	}

	public Long getProcessDefId() {
		return processDefId;
	}
}
