package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.DocumentLine;


public class GetDataResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<DocumentLine> lines = new ArrayList<DocumentLine>();

	public GetDataResponse() {
		// For serialization only
	}

	public GetDataResponse(ArrayList<DocumentLine> lines) {
		this.lines = lines;
	}

	public ArrayList<DocumentLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<DocumentLine> lines) {
		this.lines = lines;
	}

}
