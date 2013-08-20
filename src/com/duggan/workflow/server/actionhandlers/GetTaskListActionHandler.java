package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * 
 * @author duggan
 * 
 */
public class GetTaskListActionHandler extends
		BaseActionHandler<GetTaskList, GetTaskListResult> {

	@Inject
	public GetTaskListActionHandler() {
	}

	@Override
	public void execute(GetTaskList action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		TaskType type = action.getType();

		List<DocSummary> summary = new ArrayList<>();
		DocStatus status = null;
		
		switch (type) {
		case DRAFT:
			status = DocStatus.DRAFTED;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case APPROVED:
			status = DocStatus.APPROVED;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case INPROGRESS:
			status = DocStatus.INPROGRESS;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case REJECTED:
			status = DocStatus.REJECTED;
			summary = DocumentDaoHelper.getAllDocuments(status);
			break;
		case SEARCH:
			Document doc = DocumentDaoHelper.getDocumentByProcessInstance(action.getProcessInstanceId());
			if(doc!=null)
				summary.add(doc);
			
			List<HTSummary> tasks = JBPMHelper.get().getTasksForUser(action.getUserId(), action.getProcessInstanceId());
			
			if(tasks!=null){
				summary.addAll(tasks);
			}
			break;
			
		default:
			summary = getPendingApprovals(action.getUserId(), type);
			break;
		}

		GetTaskListResult result = (GetTaskListResult) actionResult;
		
		Collections.sort(summary);
		
		result.setTasks(summary);

	}

	private List<DocSummary> getPendingApprovals(String userId, TaskType type) {

		List<HTSummary> tasks = new ArrayList<>();

		try {
			tasks = JBPMHelper.get().getTasksForUser(userId, type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<DocSummary> summary = new ArrayList<>();
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
