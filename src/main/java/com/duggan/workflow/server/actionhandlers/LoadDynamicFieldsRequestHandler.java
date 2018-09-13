package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.requests.LoadDynamicFieldsRequest;
import com.duggan.workflow.shared.responses.LoadDynamicFieldsResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class LoadDynamicFieldsRequestHandler
		extends
		AbstractActionHandler<LoadDynamicFieldsRequest, LoadDynamicFieldsResponse> {

	@Override
	public Class<LoadDynamicFieldsRequest> getActionType() {
		return LoadDynamicFieldsRequest.class;
	}

	@Override
	public void execute(LoadDynamicFieldsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Doc doc = action.getDoc();
		ArrayList<Field> fields = action.getFieldNames();
		
		fields.addAll(FormDaoHelper.loadFieldValues(doc,fields));
		
		((LoadDynamicFieldsResponse)actionResult).setFields(fields);
	}

}
