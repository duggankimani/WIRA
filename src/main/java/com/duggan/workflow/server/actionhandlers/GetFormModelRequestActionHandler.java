package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.drools.definition.process.Node;
import org.jbpm.task.Task;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

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
		Long id = action.getId();
		Long parentId = action.getParentId();

		GetFormModelResponse response = (GetFormModelResponse) actionResult;

		FormModel model = null;

		List<FormModel> models = new ArrayList<>();
		switch (requestFor) {
		case FormModel.FORMMODEL:
			if(id!=null){
				model = FormDaoHelper.getForm(id, loadChildrenInfo);
				models.add(model);
			}else if(action.getTaskId()!=null){
				//model = FormDaoHelper.getFormByName(JBPMHelper.get().getTaskName(action.getTaskId()));
				Long taskId = action.getTaskId();
				List<TaskStepDTO> steps= ProcessDefHelper.getTaskStepsByTaskId(taskId);
				if(steps.size()>0){
					TaskStepDTO step = steps.get(0);
					model = FormDaoHelper.getForm(step.getFormId(),true);
				}
				
				if(model!=null)
					models.add(model);
			}else if(action.getDocRefId()!=null){
				
				//ADDocType type = DB.getDocumentDao().getDocumentTypeByDocumentId(action.getDocumentId());
				
				//List<TaskStepDTO> steps= ProcessDefHelper.getTaskStepsByDocumentId(action.getDocumentId());
				List<TaskStepDTO> steps= ProcessDefHelper.getTaskStepsByDocRefId(action.getDocRefId());
				
				if(steps.size()>0){
					TaskStepDTO step = steps.get(0);
					model = FormDaoHelper.getForm(step.getFormId(),true);
					models.add(model);
				}
												
			}else if(action.getDocumentId()!=null){
				
				//ADDocType type = DB.getDocumentDao().getDocumentTypeByDocumentId(action.getDocumentId());
				
				List<TaskStepDTO> steps= ProcessDefHelper.getTaskStepsByDocumentId(action.getDocumentId());
				
				if(steps.size()>0){
					TaskStepDTO step = steps.get(0);
					model = FormDaoHelper.getForm(step.getFormId(),true);
					models.add(model);
				}
												
			}else if(action.getProcessDefId()!=null){
				models.addAll(FormDaoHelper.getForms(action.getProcessDefId()));
			}
			break;

		case FormModel.FIELDMODEL:
			
			if(id!=null){
				model = FormDaoHelper.getField(id);
				models.add(model);
			}else if(parentId!=null){
				models.addAll(FormDaoHelper.getFields(parentId,true));
			}
			
			break;

		case FormModel.PROPERTYMODEL:
			model = FormDaoHelper.getProperty(id);
			models.add(model);
			break;

		}
		
		response.setFormModel(models);

	}

	@Override
	public Class<GetFormModelRequest> getActionType() {
		return GetFormModelRequest.class;
	}
}
