package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AssignTaskEvent extends GwtEvent<AssignTaskEvent.AssignTaskHandler> {

	public static Type<AssignTaskHandler> TYPE = new Type<AssignTaskHandler>();

	private Long taskId;
	private String userId;
	
	public interface AssignTaskHandler extends EventHandler {
		void onAssignTask(AssignTaskEvent event);
	}

	public AssignTaskEvent(Long taskId, String userId) {
		this.taskId = taskId;
		this.userId = userId;
	}

	@Override
	protected void dispatch(AssignTaskHandler handler) {
		handler.onAssignTask(this);
	}

	@Override
	public Type<AssignTaskHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AssignTaskHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long taskId, String userId) {
		source.fireEvent(new AssignTaskEvent(taskId, userId));
	}

	public static Type<AssignTaskHandler> getTYPE() {
		return TYPE;
	}

	public Long getTaskId() {
		return taskId;
	}

	public String getUserId() {
		return userId;
	}
}
