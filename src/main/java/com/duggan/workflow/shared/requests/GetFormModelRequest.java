package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetFormModelRequest extends BaseRequest<GetFormModelResponse> {

	private String type;
	private String formRefId;
	private String parentRefId;
	private Long taskId;
	private Long documentId;
	private Boolean loadChildrenToo;
	private Long processDefId;
	private String docRefId;
	

	@SuppressWarnings("unused")
	private GetFormModelRequest() {
	}

	public GetFormModelRequest(String type, String formRefId, Boolean loadChildrenToo) {
		this.type = type;
		this.formRefId = formRefId;
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

	public Boolean getLoadChildrenToo() {
		return loadChildrenToo;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetFormModelResponse();
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

	public String getFormRefId() {
		return formRefId;
	}

	public void setFormRefId(String formRefId) {
		this.formRefId = formRefId;
	}

	public String getParentRefId() {
		return parentRefId;
	}

	public void setParentRefId(String parentRefId) {
		this.parentRefId = parentRefId;
	}

}
