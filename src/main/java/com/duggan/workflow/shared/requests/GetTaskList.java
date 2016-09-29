package com.duggan.workflow.shared.requests;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskList extends BaseListRequest<GetTaskListResult> {

	private String processRefId;
	private String userId;
	private TaskType type;
	private Long processInstanceId;
	private String docRefId;
	private Long documentId;
	private SearchFilter filter;
	private boolean isLoadAsAdmin=false;
	
	@SuppressWarnings("unused")
	private GetTaskList() {
	}

	public GetTaskList(String processRefId,String userId, TaskType type) {
		this.processRefId = processRefId;
		this.userId = userId;
		this.type = type;
		
	}

	public GetTaskList(String processRefId,String userId, SearchFilter filter) {
		this.processRefId = processRefId;
		this.userId = userId;
		this.filter = filter;
		this.type=TaskType.SEARCH;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public TaskType getType() {
		return type;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskListResult();
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public SearchFilter getFilter() {
		return filter;
	}

	@Deprecated
	public Long getDocumentId() {
		return documentId;
	}

	@Deprecated
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public boolean isLoadAsAdmin() {
		return isLoadAsAdmin;
	}

	public void setLoadAsAdmin(boolean isLoadAsAdmin) {
		this.isLoadAsAdmin = isLoadAsAdmin;
	}

	public String getDocRefId() {
		return docRefId;
	}

	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}
	
	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

}
