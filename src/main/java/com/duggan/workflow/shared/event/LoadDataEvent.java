package com.duggan.workflow.shared.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class LoadDataEvent extends GwtEvent<LoadDataEvent.LoadDataHandler> {

	public static Type<LoadDataHandler> TYPE = new Type<LoadDataHandler>();
	private Integer page;

	public interface LoadDataHandler extends EventHandler {
		void onLoadData(LoadDataEvent event);
	}

	public LoadDataEvent(Integer page) {
		this.page = page;
	}

	@Override
	protected void dispatch(LoadDataHandler handler) {
		handler.onLoadData(this);
	}

	@Override
	public Type<LoadDataHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadDataHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Integer taskId) {
		source.fireEvent(new LoadDataEvent(taskId));
	}

	public Integer getPage() {
		return page;
	}
}
