package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.NotificationCategory;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NotificationCategoryChangeEvent extends
		GwtEvent<NotificationCategoryChangeEvent.NoticationCategoryChangeHandler> {

	public static Type<NoticationCategoryChangeHandler> TYPE = new Type<NoticationCategoryChangeHandler>();
	private NotificationCategory category;
	private Long processDefId;
	private Long nodeId;
	private String stepName;

	public interface NoticationCategoryChangeHandler extends EventHandler {
		void onNoticationCategoryChange(NotificationCategoryChangeEvent event);
	}

	public NotificationCategoryChangeEvent(NotificationCategory category, Long nodeId, String stepName, 
			Long processDefId) {
		this.category = category;
		this.nodeId = nodeId;
		this.stepName = stepName;
		this.processDefId = processDefId;
	}

	@Override
	protected void dispatch(NoticationCategoryChangeHandler handler) {
		handler.onNoticationCategoryChange(this);
	}

	@Override
	public Type<NoticationCategoryChangeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NoticationCategoryChangeHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, NotificationCategory category,Long nodeId, String stepName, 
			Long processDefId) {
		source.fireEvent(new NotificationCategoryChangeEvent(category,nodeId, stepName, processDefId));
	}

	public NotificationCategory getCategory() {
		return category;
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public String getStepName() {
		return stepName;
	}
}
