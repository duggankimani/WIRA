package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.TaskType;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * 
 * @author duggan
 * 
 */
public class GetTaskListActionHandler extends
		AbstractActionHandler<GetTaskList, GetTaskListResult> {

	@Inject
	public GetTaskListActionHandler() {
	}

	@Override
	public void execute(GetTaskList action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		String userId = action.getUserId()==null? SessionHelper.getCurrentUser().getUserId():
			action.getUserId();
		
		TaskType type = action.getType();

		List<Doc> summary = new ArrayList<>();
		DocStatus status = null;
		
		switch (type) {
		case DRAFT:
			status = DocStatus.DRAFTED;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
//		case APPROVED:
//			status = DocStatus.APPROVED;
//			summary = DocumentDaoHelper.getAllDocuments(status);
//			break;
//		case INPROGRESS:
//			status = DocStatus.INPROGRESS;
//			summary = DocumentDaoHelper.getAllDocuments(status);
//			break;
		case PARTICIPATED:
			summary = DocumentDaoHelper.getAllDocuments(DocStatus.INPROGRESS, DocStatus.REJECTED,DocStatus.APPROVED,
					DocStatus.COMPLETED);
			summary.addAll(getPendingApprovals(userId, type));
			break;
//		case REJECTED:
//			status = DocStatus.REJECTED;
//			summary = DocumentDaoHelper.getAllDocuments(status);
//			break;
		case SEARCH:
			
			if(action.getFilter()!=null){				
				summary.addAll(DocumentDaoHelper.search(userId,action.getFilter()));
				summary.addAll(JBPMHelper.get().searchTasks(userId, action.getFilter()));
			}else if(action.getProcessInstanceId()!=null || action.getDocRefId()!=null){
				
				Long processInstanceId = action.getProcessInstanceId();
				
				if(processInstanceId==null || processInstanceId==0L){
					processInstanceId = 
							DocumentDaoHelper.getProcessInstanceIdByDocRefId(action.getDocRefId());
				}
								
				//Document doc = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId);
			
//				if(action.getDocumentId()!=null){
//					doc = DocumentDaoHelper.getDocument(action.getDocumentId());
//				}else if(processInstanceId!=null){
//					doc = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId);
//				}
				
	
				Document doc = null;
				
				if(processInstanceId!=null){
					//ensure current user has rights to view
					doc = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId,true);
				}else if(action.getDocRefId()!=null){
					doc = DocumentDaoHelper.getDocument(action.getDocRefId(),true);
				}
				
				if(doc!=null)
					summary.add(doc);
				
				List<HTSummary> tasks = JBPMHelper.get().getTasksForUser(userId, processInstanceId, action.isLoadAsAdmin());
				
				if(tasks!=null){
					summary.addAll(tasks);
				}
			}
			
			break;
			
		default:
			//Tasks loaded here
			summary = getPendingApprovals(userId, type);
			break;
		}

		GetTaskListResult result = (GetTaskListResult) actionResult;
		
		Collections.sort(summary);
		
		result.setTasks(summary);

	}

	private List<Doc> getPendingApprovals(String userId, TaskType type) {

		List<HTSummary> tasks = new ArrayList<>();

		try {
			tasks = JBPMHelper.get().getTasksForUser(userId, type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Doc> summary = new ArrayList<>();
		for (HTSummary s : tasks) {
			summary.add(s);
		}

		return summary;
	}

	@Override
	public void undo(GetTaskList action, GetTaskListResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetTaskList> getActionType() {
		return GetTaskList.class;
	}
}
