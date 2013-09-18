package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import com.google.gwt.event.shared.HasHandlers;

public class EditProcessEvent extends
		GwtEvent<EditProcessEvent.EditProcessHandler> {

	public static Type<EditProcessHandler> TYPE = new Type<EditProcessHandler>();
	private Long processId;

	public interface EditProcessHandler extends EventHandler {
		void onEditProcess(EditProcessEvent event);
	}

	public EditProcessEvent(Long processId) {
		this.processId = processId;
	}

	public Long getProcessId() {
		return processId;
	}

	@Override
	protected void dispatch(EditProcessHandler handler) {
		handler.onEditProcess(this);
	}

	@Override
	public Type<EditProcessHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditProcessHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long processId) {
		source.fireEvent(new EditProcessEvent(processId));
	}
}
