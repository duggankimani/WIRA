package com.duggan.workflow.client.ui.upload.attachment;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ShowIframeEvent extends
		GwtEvent<ShowIframeEvent.ShowIframeHandler> {

	public static Type<ShowIframeHandler> TYPE = new Type<ShowIframeHandler>();
	private String uri;
	private String title;

	public interface ShowIframeHandler extends EventHandler {
		void onShowIframe(ShowIframeEvent event);
	}

	public ShowIframeEvent(String uri, String title) {
		this.uri = uri;
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	@Override
	protected void dispatch(ShowIframeHandler handler) {
		handler.onShowIframe(this);
	}

	@Override
	public Type<ShowIframeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ShowIframeHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String uri, String title) {
		source.fireEvent(new ShowIframeEvent(uri,title));
	}

	public String getTitle() {
		return title;
	}
}
