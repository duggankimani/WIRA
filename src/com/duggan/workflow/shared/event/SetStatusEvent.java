package com.duggan.workflow.shared.event;

import com.duggan.workflow.shared.model.HTStatus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class SetStatusEvent extends GwtEvent<SetStatusEvent.SetStatusHandler> {

	public static Type<SetStatusHandler> TYPE = new Type<SetStatusHandler>();

	private HTStatus status;
	
	public interface SetStatusHandler extends EventHandler {
		void onSetStatus(SetStatusEvent event);
	}

	public SetStatusEvent(HTStatus status) {
		this.status = status;
	}

	@Override
	protected void dispatch(SetStatusHandler handler) {
		handler.onSetStatus(this);
	}

	@Override
	public Type<SetStatusHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SetStatusHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, HTStatus status) {
		source.fireEvent(new SetStatusEvent(status));
	}

	public HTStatus getStatus() {
		return status;
	}

	public void setStatus(HTStatus status) {
		this.status = status;
	}
}
