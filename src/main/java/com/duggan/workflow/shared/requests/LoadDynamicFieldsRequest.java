package com.duggan.workflow.shared.requests;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.LoadDynamicFieldsResponse;

/**
 * 
 * @author duggan
 *
 */
public class LoadDynamicFieldsRequest extends BaseRequest<LoadDynamicFieldsResponse> {

	private Doc doc;
	private List<Field> fieldNames = new ArrayList<Field>();
	
	public LoadDynamicFieldsRequest() {
	}
	
	public LoadDynamicFieldsRequest(Doc doc, List<Field> fields){
		this.doc = doc;
		this.fieldNames = fields;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new LoadDynamicFieldsResponse();
	}

	public Doc getDoc() {
		return doc;
	}

	public List<Field> getFieldNames() {
		return fieldNames;
	}
}
