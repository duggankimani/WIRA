package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ExecTriggerEvent extends
		GwtEvent<ExecTriggerEvent.ExecTriggerHandler> {

	public static Type<ExecTriggerHandler> TYPE = new Type<ExecTriggerHandler>();
	private String requestType;
	private boolean isValidateForm=true;

	public interface ExecTriggerHandler extends EventHandler {
		void onExecTrigger(ExecTriggerEvent event);
	}

	private String triggerName;

	public ExecTriggerEvent(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getRequestType() {
		return requestType;
	}

	@Override
	protected void dispatch(ExecTriggerHandler handler) {
		handler.onExecTrigger(this);
	}

	@Override
	public Type<ExecTriggerHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ExecTriggerHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String triggerType) {
		source.fireEvent(new ExecTriggerEvent(triggerType));
	}

	public boolean isValidateForm() {
		return isValidateForm;
	}

	public void setValidateForm(boolean isValidateForm) {
		this.isValidateForm = isValidateForm;
	}

	public String getTriggerName() {
		return triggerName;
	}

}
