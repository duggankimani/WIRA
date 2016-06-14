package com.duggan.workflow.client.ui.events;

import java.util.ArrayList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UploadEndedEvent extends
		GwtEvent<UploadEndedEvent.UploadEndedHandler> {

	public static Type<UploadEndedHandler> TYPE = new Type<UploadEndedHandler>();

	public interface UploadEndedHandler extends EventHandler {
		void onUploadEnded(UploadEndedEvent event);
	}
	
	private Object source;
	private ArrayList<String> fileFieldNames;

	public UploadEndedEvent(Object source) {
		this.source = source;
	}
	
	public UploadEndedEvent(Object source, ArrayList<String> fileFieldNames) {
		this.source = source;
		this.fileFieldNames = fileFieldNames;
	}

	@Override
	protected void dispatch(UploadEndedHandler handler) {
		handler.onUploadEnded(this);
	}

	@Override
	public Type<UploadEndedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UploadEndedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new UploadEndedEvent(source));
	}

	public Object getSource() {
		return source;
	}

	public ArrayList<String> getFileFieldNames() {
		return fileFieldNames;
	}
}
