package com.duggan.workflow.shared.responses;

public class DeleteDocumentResponse extends BaseResponse {

	private boolean isDelete;

	public DeleteDocumentResponse() {
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

}
