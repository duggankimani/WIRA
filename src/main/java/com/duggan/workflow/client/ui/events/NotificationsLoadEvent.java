package com.duggan.workflow.client.ui.events;

import java.util.List;

import com.duggan.workflow.shared.model.Notification;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NotificationsLoadEvent extends
		GwtEvent<NotificationsLoadEvent.NotificationsLoadHandler> {

	public static Type<NotificationsLoadHandler> TYPE = new Type<NotificationsLoadHandler>();
	private List notifications;

	public interface NotificationsLoadHandler extends EventHandler {
		void onNotificationsLoad(NotificationsLoadEvent event);
	}

	public NotificationsLoadEvent(List notifications) {
		this.notifications = notifications;
	}

	public List<Notification> getNotifications() {
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

	public static void fire(HasHandlers source, List notifications) {
		source.fireEvent(new NotificationsLoadEvent(notifications));
	}
}
