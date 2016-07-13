package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.response.BaseResponse;


public class ExportFormResponse extends BaseResponse {

	private String xml;

	public ExportFormResponse() {
		// For serialization only
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}
