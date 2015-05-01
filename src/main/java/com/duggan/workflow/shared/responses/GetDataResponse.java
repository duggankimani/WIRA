package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.DocumentLine;


public class GetDataResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DocumentLine> lines = new ArrayList<DocumentLine>();

	public GetDataResponse() {
		// For serialization only
	}

	public GetDataResponse(List<DocumentLine> lines) {
		this.lines = lines;
	}

	public List<DocumentLine> getLines() {
		return lines;
	}

	public void setLines(List<DocumentLine> lines) {
		this.lines = lines;
	}

}
