package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.DeleteTableRowResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteTableRowRequest extends BaseRequest<DeleteTableRowResponse> {

	private String fieldRefId;
	private int row;

	@SuppressWarnings("unused")
	private DeleteTableRowRequest() {
		// For serialization only
	}

	public DeleteTableRowRequest(String fieldRefId, int row) {
		this.fieldRefId = fieldRefId;
		this.row = row;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new DeleteTableRowResponse();
	}

	public int getRow() {
		return row;
	}

	public String getFieldRefId() {
		return fieldRefId;
	}

}
