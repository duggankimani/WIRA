package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.form.Field;

/**
 * 
 * @author duggan
 *
 */
public class LoadDynamicFieldsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Field> fields = new ArrayList<Field>();
	
	public LoadDynamicFieldsResponse() {
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
}
