package com.duggan.workflow.shared.responses;

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
