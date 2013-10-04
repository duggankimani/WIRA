package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.helper.dao.FormDaoHelper;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetFormModelRequestActionHandler extends
		BaseActionHandler<GetFormModelRequest, GetFormModelResponse> {

	@Inject
	public GetFormModelRequestActionHandler() {
	}

	@Override
	public void execute(GetFormModelRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		String requestFor = action.getType();
		Boolean loadChildrenInfo = action.getLoadChildrenToo();
		Long id = action.getId();
		Long parentId = action.getParentId();

		GetFormModelResponse response = (GetFormModelResponse) actionResult;

		FormModel model = null;

		List<FormModel> models = new ArrayList<>();
		switch (requestFor) {
		case FormModel.FORMMODEL:
			if(id!=null){
				model = FormDaoHelper.getForm(id, loadChildrenInfo);
				response.addItem(model);
			}else{
				models.addAll(FormDaoHelper.getForms());
			}
			break;

		case FormModel.FIELDMODEL:
			
			if(id!=null){
				model = FormDaoHelper.getField(id);
				response.addItem(model);
			}else if(parentId!=null){
				models.addAll(FormDaoHelper.getFields(parentId,true));
			}
			break;

		case FormModel.PROPERTYMODEL:
			model = FormDaoHelper.getProperty(id);
			response.addItem(model);
			break;

		}
		
		response.setFormModel(models);

	}

	@Override
	public Class<GetFormModelRequest> getActionType() {
		return GetFormModelRequest.class;
	}
}
