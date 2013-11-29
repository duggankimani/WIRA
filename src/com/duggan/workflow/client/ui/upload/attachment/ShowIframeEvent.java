package com.duggan.workflow.client.ui.upload.attachment;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

public class ShowIframeEvent extends
		GwtEvent<ShowIframeEvent.ShowIframeHandler> {

	public static Type<ShowIframeHandler> TYPE = new Type<ShowIframeHandler>();
	private String uri;

	public interface ShowIframeHandler extends EventHandler {
		void onShowIframe(ShowIframeEvent event);
	}

	public ShowIframeEvent(String uri) {
		this.uri = uri;
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

	public static void fire(HasHandlers source, String uri) {
		source.fireEvent(new ShowIframeEvent(uri));
	}
}
