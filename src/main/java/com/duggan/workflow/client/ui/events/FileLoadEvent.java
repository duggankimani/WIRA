package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class FileLoadEvent extends GwtEvent<FileLoadEvent.FileLoadHandler> {

	public static Type<FileLoadHandler> TYPE = new Type<FileLoadHandler>();
	private Attachment attachment;

	public interface FileLoadHandler extends EventHandler {
		void onFileLoad(FileLoadEvent event);
	}

	public FileLoadEvent(Attachment attachment) {
		this.attachment = attachment;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	@Override
	protected void dispatch(FileLoadHandler handler) {
		handler.onFileLoad(this);
	}

	@Override
	public Type<FileLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<FileLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Attachment attachment) {
		source.fireEvent(new FileLoadEvent(attachment));
	}
}
