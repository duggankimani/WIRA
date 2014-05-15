package com.duggan.workflow.client.ui.events;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AlertLoadEvent extends GwtEvent<AlertLoadEvent.AlertLoadHandler> {

	public static Type<AlertLoadHandler> TYPE = new Type<AlertLoadHandler>();
	private HashMap<TaskType, Integer> alerts;

	public interface AlertLoadHandler extends EventHandler {
		void onAlertLoad(AlertLoadEvent event);
	}

	public AlertLoadEvent(HashMap<TaskType, Integer> alerts) {
		this.alerts = alerts;
	}

	public HashMap<TaskType, Integer> getAlerts() {
		return alerts;
	}

	@Override
	protected void dispatch(AlertLoadHandler handler) {
		handler.onAlertLoad(this);
	}

	@Override
	public Type<AlertLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AlertLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, HashMap<TaskType, Integer> alerts) {
		source.fireEvent(new AlertLoadEvent(alerts));
	}
}
