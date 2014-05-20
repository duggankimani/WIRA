package com.duggan.workflow.client.ui.events;

import java.util.List;
import java.util.Map;

import com.duggan.workflow.shared.model.Activity;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ActivitiesLoadEvent extends
		GwtEvent<ActivitiesLoadEvent.ActivitiesLoadHandler> {

	public static Type<ActivitiesLoadHandler> TYPE = new Type<ActivitiesLoadHandler>();
	private Map<Activity, List<Activity>> activitiesMap;

	public interface ActivitiesLoadHandler extends EventHandler {
		void onActivitiesLoad(ActivitiesLoadEvent event);
	}

	public ActivitiesLoadEvent(Map<Activity, List<Activity>> activitiesMap) {
		this.activitiesMap = activitiesMap;
	}

	public Map<Activity, List<Activity>> getActivitiesMap() {
		return activitiesMap;
	}

	@Override
	protected void dispatch(ActivitiesLoadHandler handler) {
		handler.onActivitiesLoad(this);
	}

	@Override
	public Type<ActivitiesLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ActivitiesLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Map<Activity, List<Activity>> activitiesMap) {
		source.fireEvent(new ActivitiesLoadEvent(activitiesMap));
	}
}
