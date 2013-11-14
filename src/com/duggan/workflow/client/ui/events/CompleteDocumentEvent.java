package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Value;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Boolean;
import java.util.Map;

import com.google.gwt.event.shared.HasHandlers;

public class CompleteDocumentEvent extends
		GwtEvent<CompleteDocumentEvent.CompleteDocumentHandler> {

	public static Type<CompleteDocumentHandler> TYPE = new Type<CompleteDocumentHandler>();
	private Long taskId;
	Map<String, Value> results;

	public interface CompleteDocumentHandler extends EventHandler {
		void onCompleteDocument(CompleteDocumentEvent event);
	}

	public CompleteDocumentEvent(Long taskId, Map<String, Value> results) {
		this.taskId = taskId;
		this.results = results;
	}


	@Override
	protected void dispatch(CompleteDocumentHandler handler) {
		handler.onCompleteDocument(this);
	}

	@Override
	public Type<CompleteDocumentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CompleteDocumentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long taskId, Map<String, Value> results) {
		source.fireEvent(new CompleteDocumentEvent(taskId, results));
	}

	public Long getTaskId() {
		return taskId;
	}


	public Map<String, Value> getResults() {
		return results;
	}
}
