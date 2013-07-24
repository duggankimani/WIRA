package com.duggan.workflow.shared.requests;

import java.lang.Integer;

import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetErrorRequestResult;

/**
 * This is the Request/Command/Action Object for retrieving
 * Errors from the database  
 * 
 * @author duggan
 *
 */
public class GetErrorRequest extends BaseRequest<GetErrorRequestResult> {

	private Integer errorId;

	@SuppressWarnings("unused")
	private GetErrorRequest() {
		// For serialization only
	}

	public GetErrorRequest(Integer errorId) {
		this.errorId = errorId;
	}

	@Override
	public BaseResult createDefaultActionResponse() {
		
		return new GetErrorRequestResult();
	}
	
	public Integer getErrorId() {
		return errorId;
	}
}
