package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.response.BaseResponse;

public class DeleteLineResponse extends BaseResponse {

	private boolean isDelete;

	public DeleteLineResponse() {
	}

	public DeleteLineResponse(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

}
