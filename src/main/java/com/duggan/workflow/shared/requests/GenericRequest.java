package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.responses.GenericResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GenericRequest extends BaseRequest<GenericResponse> {

	private HashMap<String, Value> values;
	private String handlerClass;

	@SuppressWarnings("unused")
	private GenericRequest() {
		// For serialization only
	}

	public GenericRequest(HashMap<String, Value> values, String handlerClass) {
		this.values = values;
		this.handlerClass = handlerClass;
	}

	public HashMap<String, Value> getValues() {
		return values;
	}

	public String getHandlerClass() {
		return handlerClass;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GenericResponse();
	}
}
