package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFormModelResponse;

import java.lang.String;
import java.lang.Long;
import java.lang.Boolean;

public class GetFormModelRequest extends BaseRequest<GetFormModelResponse> {

	private String type;
	private Long id;
	private Long parentId;
	private Long taskId;
	private Long documentId;
	private Boolean loadChildrenToo;

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

	public Long getDocumentId() {
		return documentId;
	}
}
