package com.duggan.workflow.client.ui.upload.attachment;

import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ShowAttachmentEvent extends
		GwtEvent<ShowAttachmentEvent.ShowAttachmentHandler> {

	public static Type<ShowAttachmentHandler> TYPE = new Type<ShowAttachmentHandler>();
	private String uri;
	private String title;
	private REPORTVIEWIMPL viewImplementation;

	public interface ShowAttachmentHandler extends EventHandler {
		void onShowAttachment(ShowAttachmentEvent event);
	}

	public ShowAttachmentEvent(String uri, String title) {
		this.uri = uri;
		this.title = title;
	}
	
	public ShowAttachmentEvent(String uri, String title, REPORTVIEWIMPL viewImplementation) {
		this.uri = uri;
		this.title = title;
		this.viewImplementation = viewImplementation;
	}

	public String getUri() {
		return uri;
	}

	@Override
	protected void dispatch(ShowAttachmentHandler handler) {
		handler.onShowAttachment(this);
	}

	@Override
	public Type<ShowAttachmentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ShowAttachmentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String uri, String title) {
		source.fireEvent(new ShowAttachmentEvent(uri,title));
	}
	
	public static void fire(HasHandlers source, String uri, String title, REPORTVIEWIMPL viewImplementation) {
		source.fireEvent(new ShowAttachmentEvent(uri,title, viewImplementation));
	}

	public String getTitle() {
		return title;
	}

	public REPORTVIEWIMPL getViewImplementation() {
		return viewImplementation;
	}
}
