package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Schema;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessSchemaResponse extends BaseResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Schema> schema;

	public ArrayList<Schema> getSchema() {
		return schema;
	}

	public void setSchema(ArrayList<Schema> schema) {
		this.schema = schema;
	}

	public GetProcessSchemaResponse() {
	}

}
