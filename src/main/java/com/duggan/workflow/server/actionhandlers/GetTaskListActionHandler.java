package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

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

		String processRefId = action.getProcessRefId();
		String processId = null;
		if(processRefId!=null){
			processId = DB.getProcessDao().getProcessId(processRefId);
		}
		
		String userId = action.getUserId()==null? SessionHelper.getCurrentUser().getUserId():
			action.getUserId();
		
		TaskType type = action.getType();

		List<Doc> summary = new ArrayList<>();
		DocStatus status = null;
		
		switch (type) {
		case DRAFT:
			status = DocStatus.DRAFTED;
			summary = DocumentDaoHelper.getAllDocumentsJson(processId,action.getOffset(),action.getLength(),false,status);
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
			summary = DocumentDaoHelper.getAllDocumentsJson(processId,action.getOffset(),action.getLength(),false,
					DocStatus.INPROGRESS, DocStatus.REJECTED,DocStatus.APPROVED,
					 DocStatus.COMPLETED);//Current users sent requests
			summary.addAll(getPendingApprovals(processId,userId, type,action.getOffset(),action.getLength()));
			break;
//		case REJECTED:
//			status = DocStatus.REJECTED;
//			summary = DocumentDaoHelper.getAllDocuments(status);
//			break;
		case SEARCH:
			
			if(action.getFilter()!=null){				
				summary.addAll(DocumentDaoHelper.search(processId,userId,action.getFilter()));
				summary.addAll(JBPMHelper.get().searchTasks(processId,userId, action.getFilter()));
			}else if(action.getProcessInstanceId()!=null || action.getDocRefId()!=null){
				
				Long processInstanceId = action.getProcessInstanceId();
				
				if(processInstanceId==null || processInstanceId==0L){
					processInstanceId = 
							DocumentDaoHelper.getProcessInstanceIdByDocRefId(action.getDocRefId());
				}
								
				Document doc = null;
				
				if(processInstanceId!=null){
					//ensure current user has rights to view
					doc = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId,true);
				}else if(action.getDocRefId()!=null){
					//doc = DocumentDaoHelper.getDocument(action.getDocRefId(),true);
					doc = DocumentDaoHelper.getDocJson(action.getDocRefId(),true);
				}
				
				if(doc!=null)
					summary.add(doc);
				
				List<HTSummary> tasks = JBPMHelper.get().getTasksForUser(processId,userId, processInstanceId, action.isLoadAsAdmin(),action.getOffset(),action.getLength());
				
				if(tasks!=null){
					summary.addAll(tasks);
				}
			}
			
			break;
			
		default:
			//Tasks loaded here
			summary = getPendingApprovals(processId,userId, type,action.getOffset(),action.getLength());
			break;
		}

		GetTaskListResult result = (GetTaskListResult) actionResult;
		
		Collections.sort(summary);
		
		result.setTasks((ArrayList<Doc>) summary);

	}

	private List<Doc> getPendingApprovals(String processId,String userId, TaskType type, int offset, int length) {

		List<HTSummary> tasks = new ArrayList<>();

		try {
			tasks = JBPMHelper.get().getTasksForUser(processId,userId, type,offset, length);
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
