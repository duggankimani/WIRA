package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseListResponse;
import com.wira.commons.shared.request.BaseRequest;

public class BaseListRequest<T extends BaseListResponse> extends BaseRequest<T>{

	private int offset;
	private int length;
	
	public BaseListRequest() {
	}
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
