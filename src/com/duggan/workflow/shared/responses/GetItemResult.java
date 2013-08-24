package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.DocSummary;

public class GetItemResult extends BaseResponse {

	DocSummary summary;

	public GetItemResult() {
		
	}

	public DocSummary getSummary() {
		return summary;
	}

	public void setSummary(DocSummary summary) {
		this.summary = summary;
	}
}
