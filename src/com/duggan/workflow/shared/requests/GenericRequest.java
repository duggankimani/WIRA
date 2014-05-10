package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GenericResponse;

import java.util.Map;
import java.lang.String;

public class GenericRequest extends BaseRequest<GenericResponse> {

	private Map<String, Value> values;
	private String handlerClass;

	@SuppressWarnings("unused")
	private GenericRequest() {
		// For serialization only
	}

	public GenericRequest(Map<String, Value> values, String handlerClass) {
		this.values = values;
		this.handlerClass = handlerClass;
	}

	public Map<String, Value> getValues() {
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
