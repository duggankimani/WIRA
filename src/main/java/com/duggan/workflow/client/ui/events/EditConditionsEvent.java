package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.TaskStepDTO;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditConditionsEvent extends GwtEvent<EditConditionsEvent.EditConditionsHandler> {
    private static Type<EditConditionsHandler> TYPE = new Type<EditConditionsHandler>();
    
    public interface EditConditionsHandler extends EventHandler {
        void onEditConditions(EditConditionsEvent event);
    }
    
    
	private TaskStepDTO taskStepDto;
   
    public EditConditionsEvent(final TaskStepDTO taskStepDto) {
		this.taskStepDto = taskStepDto;
    }
    
    public static Type<EditConditionsHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final EditConditionsHandler handler) {
        handler.onEditConditions(this);
    }

    @Override
    public Type<EditConditionsHandler> getAssociatedType() {
        return TYPE;
    }

	public TaskStepDTO getTaskStepDto() {
		return taskStepDto;
	}

	public void setTaskStepDto(TaskStepDTO taskStepDto) {
		this.taskStepDto = taskStepDto;
	}
    
}