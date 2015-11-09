package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.NotificationCategory;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.CheckPasswordRequestResult;
import com.duggan.workflow.shared.responses.GetNotificationTemplateResult;

public class GetNotificationTemplateRequest extends
		BaseRequest<GetNotificationTemplateResult> {

	private Long nodeId;
	private String stepName;
	private Long processDefId;
	private NotificationCategory category;
	private Actions action;
	
	@SuppressWarnings("unused")
	private GetNotificationTemplateRequest() {
		// For serialization only
	}

	public GetNotificationTemplateRequest(NotificationCategory category,String stepName,
			Long nodeId, Long processDefId, Actions action) {
		this.nodeId = nodeId;
		this.stepName = stepName;
		this.processDefId = processDefId;
		this.category = category;
		this.action = action;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetNotificationTemplateResult();
	}

	public Long getNodeId() {
		return nodeId;
	}

	public String getStepName() {
		return stepName;
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public NotificationCategory getCategory() {
		return category;
	}

	public Actions getAction() {
		return action;
	}
}
