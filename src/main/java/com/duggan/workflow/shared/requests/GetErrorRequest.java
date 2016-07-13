package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetErrorRequestResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

/**
 * This is the Request/Command/Action Object for retrieving
 * Errors from the database  
 * 
 * @author duggan
 *
 */
public class GetErrorRequest extends BaseRequest<GetErrorRequestResult> {

	private Long errorId;

	@SuppressWarnings("unused")
	private GetErrorRequest() {
		// For serialization only
	}

	public GetErrorRequest(Long errorId) {
		this.errorId = errorId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetErrorRequestResult();
	}
	
	public Long getErrorId() {
		return errorId;
	}
}
