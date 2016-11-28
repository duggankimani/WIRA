package com.duggan.workflow.shared.events;

import com.duggan.workflow.shared.model.TaskStepDTO;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SaveTaskStepEvent extends
		GwtEvent<SaveTaskStepEvent.SaveTaskStepHandler> {

	public static Type<SaveTaskStepHandler> TYPE = new Type<SaveTaskStepHandler>();
	private TaskStepDTO taskStepDTO;

	public interface SaveTaskStepHandler extends EventHandler {
		void onSaveTaskStep(SaveTaskStepEvent event);
	}

	public SaveTaskStepEvent(TaskStepDTO taskStepDTO) {
		this.taskStepDTO  = taskStepDTO;
	}

	@Override
	protected void dispatch(SaveTaskStepHandler handler) {
		handler.onSaveTaskStep(this);
	}

	@Override
	public Type<SaveTaskStepHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SaveTaskStepHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, TaskStepDTO taskStepDTO) {
		source.fireEvent(new SaveTaskStepEvent(taskStepDTO));
	}

	public TaskStepDTO getTaskStepDTO() {
		return taskStepDTO;
	}
}
