package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Actions;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import com.google.gwt.event.shared.HasHandlers;

public class ExecTaskEvent extends GwtEvent<ExecTaskEvent.ExecTaskHandler> {

	public static Type<ExecTaskHandler> TYPE = new Type<ExecTaskHandler>();
	private Long documentId;
	private Actions action;
	
	public interface ExecTaskHandler extends EventHandler {
		void onExecTask(ExecTaskEvent event);
	}

	public ExecTaskEvent(Long documentId, Actions action) {
		this.documentId = documentId;
		this.action = action;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	public Actions getAction(){
		return action;
	}

	@Override
	protected void dispatch(ExecTaskHandler handler) {
		handler.onExecTask(this);
	}

	@Override
	public Type<ExecTaskHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ExecTaskHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long documentId, Actions action) {
		source.fireEvent(new ExecTaskEvent(documentId, action));
	}
}
