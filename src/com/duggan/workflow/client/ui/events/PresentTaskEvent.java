package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.duggan.workflow.shared.model.Doc;
import com.google.gwt.event.shared.HasHandlers;

public class PresentTaskEvent extends
		GwtEvent<PresentTaskEvent.PresentTaskHandler> {

	public static Type<PresentTaskHandler> TYPE = new Type<PresentTaskHandler>();
	private Doc doc;

	public interface PresentTaskHandler extends EventHandler {
		void onPresentTask(PresentTaskEvent event);
	}

	public PresentTaskEvent(Doc doc) {
		this.doc = doc;
	}

	public Doc getDoc() {
		return doc;
	}

	@Override
	protected void dispatch(PresentTaskHandler handler) {
		handler.onPresentTask(this);
	}

	@Override
	public Type<PresentTaskHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PresentTaskHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Doc doc) {
		source.fireEvent(new PresentTaskEvent(doc));
	}
}
