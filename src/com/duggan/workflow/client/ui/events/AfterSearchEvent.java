package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AfterSearchEvent extends
		GwtEvent<AfterSearchEvent.AfterSearchHandler> {

	public static Type<AfterSearchHandler> TYPE = new Type<AfterSearchHandler>();
	private String subject;
	private String description;

	public interface AfterSearchHandler extends EventHandler {
		void onAfterSearch(AfterSearchEvent event);
	}

	public AfterSearchEvent(String subject, String description) {
		this.subject = subject;
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public String getDescription() {
		return description;
	}

	@Override
	protected void dispatch(AfterSearchHandler handler) {
		handler.onAfterSearch(this);
	}

	@Override
	public Type<AfterSearchHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AfterSearchHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String subject, String description) {
		source.fireEvent(new AfterSearchEvent(subject, description));
	}
}
