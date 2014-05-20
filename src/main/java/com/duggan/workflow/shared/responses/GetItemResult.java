package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Doc;

public class GetItemResult extends BaseResponse {

	Doc summary;

	public GetItemResult() {
		
	}

	public Doc getSummary() {
		return summary;
	}

	public void setSummary(Doc summary) {
		this.summary = summary;
	}
}
