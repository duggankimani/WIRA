package com.duggan.workflow.client.ui.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.Activity;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ActivitiesLoadEvent extends
		GwtEvent<ActivitiesLoadEvent.ActivitiesLoadHandler> {

	public static Type<ActivitiesLoadHandler> TYPE = new Type<ActivitiesLoadHandler>();
	private HashMap<Activity, ArrayList<Activity>> activitiesMap;

	public interface ActivitiesLoadHandler extends EventHandler {
		void onActivitiesLoad(ActivitiesLoadEvent event);
	}

	public ActivitiesLoadEvent(HashMap<Activity, ArrayList<Activity>> activitiesMap) {
		this.activitiesMap = activitiesMap;
	}

	public HashMap<Activity, ArrayList<Activity>> getActivitiesMap() {
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

	public static void fire(HasHandlers source, HashMap<Activity, ArrayList<Activity>> activitiesMap) {
		source.fireEvent(new ActivitiesLoadEvent(activitiesMap));
	}
}
