package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.TreeType;
import com.duggan.workflow.shared.responses.GetFileTreeResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetFileTreeRequest extends
		BaseRequest<GetFileTreeResponse> {

	private String refId;
	private TreeType type;

	public GetFileTreeRequest() {
	}
	
	public GetFileTreeRequest with(TreeType type){
		this.type = type;
		return this;
	}
	
	public GetFileTreeRequest with(String refId){
		this.refId = refId;
		return this;
	}

		
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetFileTreeResponse();
	}

	public String getRefId() {
		return refId;
	}

	public TreeType getType() {
		return type;
	}

}
