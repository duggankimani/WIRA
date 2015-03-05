package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DeleteAttachmentEvent extends
		GwtEvent<DeleteAttachmentEvent.DeleteAttachmentHandler> {

	public static Type<DeleteAttachmentHandler> TYPE = new Type<DeleteAttachmentHandler>();
	private Attachment attachment;
	private Long [] attachmentIds;
	
	public interface DeleteAttachmentHandler extends EventHandler {
		void onDeleteAttachment(DeleteAttachmentEvent event);
	}

	public DeleteAttachmentEvent(Attachment attachment) {
		this.attachment = attachment;
	}
	
	public DeleteAttachmentEvent(Long [] attachmentIds) {
		this.attachmentIds= attachmentIds;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	@Override
	protected void dispatch(DeleteAttachmentHandler handler) {
		handler.onDeleteAttachment(this);
	}

	@Override
	public Type<DeleteAttachmentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DeleteAttachmentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Attachment attachment) {
		source.fireEvent(new DeleteAttachmentEvent(attachment));
	}

	public Long[] getAttachmentIds() {
		return attachmentIds;
	}
}
