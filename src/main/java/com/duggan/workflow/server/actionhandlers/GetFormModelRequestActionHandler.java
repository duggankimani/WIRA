package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
<<<<<<< HEAD
import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
=======
import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
>>>>>>> wiradbaccess
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetFormModelRequestActionHandler extends
		AbstractActionHandler<GetFormModelRequest, GetFormModelResponse> {

	@Inject
	public GetFormModelRequestActionHandler() {
	}

	@Override
	public void execute(GetFormModelRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		String requestFor = action.getType();
		Boolean loadChildrenInfo = action.getLoadChildrenToo();
		String formModelRefId = action.getFormRefId();
		String parentRefId = action.getParentRefId();

		GetFormModelResponse response = (GetFormModelResponse) actionResult;

		FormModel model = null;

		List<FormModel> models = new ArrayList<>();
		
		List<TaskStepDTO> steps = new ArrayList<TaskStepDTO>();
		switch (requestFor) {
		case FormModel.FORMMODEL:
			if(formModelRefId!=null){
				model = FormDaoHelper.getFormJson(formModelRefId, loadChildrenInfo);
				models.add(model);
			}else if(action.getTaskId()!=null){
				Long taskId = action.getTaskId();
				steps= ProcessDaoHelper.getTaskStepsByTaskId(taskId);
				if(steps.size()>0){
					TaskStepDTO step = steps.get(0);
					model = FormDaoHelper.getFormJson(step.getFormRefId(),true);
				}
				
				if(model!=null)
					models.add(model);
			}else if(action.getDocRefId()!=null){
				steps= ProcessDaoHelper.getTaskStepsByDocRefId(action.getDocRefId());
				
				if(steps.size()>0){
					TaskStepDTO step = steps.get(0);
					model = FormDaoHelper.getFormJson(step.getFormRefId(),true);
					models.add(model);
				}
												
			}else if(action.getDocRefId()!=null){
				steps= ProcessDaoHelper.getTaskStepsByDocumentJson(action.getDocRefId());
				
				if(steps.size()>0){
					TaskStepDTO step = steps.get(0);
					model = FormDaoHelper.getFormJson(step.getFormRefId(),true);
					models.add(model);
				}
												
			}else if(action.getProcessDefId()!=null){
				models.addAll(FormDaoHelper.getForms(action.getProcessDefId()));
			}
			
			
			break;

		case FormModel.FIELDMODEL:
			
			if(formModelRefId!=null){
				model = FormDaoHelper.getFieldJson(formModelRefId,true);
				models.add(model);
			}else if(parentRefId!=null){
				models.addAll(FormDaoHelper.getFieldJson(parentRefId).getFields());
			}
			
			break;

//		case FormModel.PROPERTYMODEL:
//			model = FormDaoHelper.getProperty(id);
//			models.add(model);
//			break;

		}
		
		response.setFormModel((ArrayList<FormModel>) models);

	}

	@Override
	public Class<GetFormModelRequest> getActionType() {
		return GetFormModelRequest.class;
	}
}
