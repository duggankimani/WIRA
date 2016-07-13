package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetFormModelRequest extends BaseRequest<GetFormModelResponse> {

	private String type;
	private Long id;
	private Long parentId;
	private Long taskId;
	private Long documentId;
	private Boolean loadChildrenToo;
	private Long processDefId;
	private String docRefId;

	@SuppressWarnings("unused")
	private GetFormModelRequest() {
	}

	public GetFormModelRequest(String type, Long id, Boolean loadChildrenToo) {
		this.type = type;
		this.id = id;
		this.loadChildrenToo = loadChildrenToo;
	}
	
	public GetFormModelRequest(String type, Long taskId){
		this.type = type;
		this.taskId = taskId;
	}
	
	public GetFormModelRequest(String type, Long taskId,String docRefId){
		this(type,taskId);
		this.docRefId = docRefId;
	}

	@Deprecated
	public GetFormModelRequest(String type, Long taskId,Long documentId){
		this(type,taskId);
		this.documentId = documentId;
	}
	
	public String getType() {
		return type;
	}

	public Long getId() {
		return id;
	}

	public Boolean getLoadChildrenToo() {
		return loadChildrenToo;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetFormModelResponse();
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@Deprecated
	public Long getDocumentId() {
		return documentId;
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}

	public String getDocRefId() {
		return docRefId;
	}
}
