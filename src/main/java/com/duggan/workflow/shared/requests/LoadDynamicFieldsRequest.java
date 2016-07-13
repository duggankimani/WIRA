package com.duggan.workflow.shared.requests;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.responses.LoadDynamicFieldsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

/**
 * 
 * @author duggan
 *
 */
public class LoadDynamicFieldsRequest extends BaseRequest<LoadDynamicFieldsResponse> {

	private Doc doc;
	private ArrayList<Field> fieldNames = new ArrayList<Field>();
	
	public LoadDynamicFieldsRequest() {
	}
	
	public LoadDynamicFieldsRequest(Doc doc, ArrayList<Field> fields){
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

	public ArrayList<Field> getFieldNames() {
		return fieldNames;
	}
}
