package com.duggan.workflow.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.gwtplatform.mvp.client.PresenterWidget;

@SuppressWarnings("rawtypes")
public class ProcessChildLoadedEvent extends
		GwtEvent<ProcessChildLoadedEvent.ProcessChildLoadedHandler> {
	private static Type<ProcessChildLoadedHandler> TYPE = new Type<ProcessChildLoadedHandler>();
	private PresenterWidget child;

	public interface ProcessChildLoadedHandler extends EventHandler {
		void onProcessChildLoaded(ProcessChildLoadedEvent event);
	}

	private String config;

	public ProcessChildLoadedEvent(final PresenterWidget child) {
		this.child = child;
	}

	public ProcessChildLoadedEvent(final PresenterWidget child, String config) {
		this.child = child;
		this.config = config;
	}

	public static Type<ProcessChildLoadedHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final ProcessChildLoadedHandler handler) {
		handler.onProcessChildLoaded(this);
	}

	@Override
	public Type<ProcessChildLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	public PresenterWidget getChild() {
		return child;
	}

	public String getConfig() {
		return config;
	}
}
