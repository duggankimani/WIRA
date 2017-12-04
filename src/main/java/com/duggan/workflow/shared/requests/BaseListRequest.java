package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseListResponse;
import com.wira.commons.shared.request.BaseRequest;

public class BaseListRequest<T extends BaseListResponse> extends BaseRequest<T>{

	private Integer offset;
	private Integer length;
	
	public BaseListRequest() {
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
	
}
