package com.duggan.workflow.shared.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Integer;
import com.google.gwt.event.shared.HasHandlers;

public class LoadTaskEvent extends GwtEvent<LoadTaskEvent.LoadTaskHandler> {

	public static Type<LoadTaskHandler> TYPE = new Type<LoadTaskHandler>();
	private Integer taskId;

	public interface LoadTaskHandler extends EventHandler {
		void onLoadTask(LoadTaskEvent event);
	}

	public LoadTaskEvent(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	@Override
	protected void dispatch(LoadTaskHandler handler) {
		handler.onLoadTask(this);
	}

	@Override
	public Type<LoadTaskHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadTaskHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Integer taskId) {
		source.fireEvent(new LoadTaskEvent(taskId));
	}
}
