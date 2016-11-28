package com.duggan.workflow.shared.events;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Notification;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NotificationsLoadEvent extends
		GwtEvent<NotificationsLoadEvent.NotificationsLoadHandler> {

	public static Type<NotificationsLoadHandler> TYPE = new Type<NotificationsLoadHandler>();
	private ArrayList notifications;

	public interface NotificationsLoadHandler extends EventHandler {
		void onNotificationsLoad(NotificationsLoadEvent event);
	}

	public NotificationsLoadEvent(ArrayList notifications) {
		this.notifications = notifications;
	}

	public ArrayList<Notification> getNotifications() {
		return notifications;
	}

	@Override
	protected void dispatch(NotificationsLoadHandler handler) {
		handler.onNotificationsLoad(this);
	}

	@Override
	public Type<NotificationsLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NotificationsLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, ArrayList notifications) {
		source.fireEvent(new NotificationsLoadEvent(notifications));
	}
}
