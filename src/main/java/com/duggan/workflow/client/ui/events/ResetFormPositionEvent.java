package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ResetFormPositionEvent extends
		GwtEvent<ResetFormPositionEvent.ResetFormPositionHandler> {

	public static Type<ResetFormPositionHandler> TYPE = new Type<ResetFormPositionHandler>();

	public interface ResetFormPositionHandler extends EventHandler {
		void onResetFormPosition(ResetFormPositionEvent event);
	}
	
	private int insertPos=-1;
	private int count;
	
	public ResetFormPositionEvent(int insertPos) {
		this.insertPos = insertPos;
	}

	@Override
	protected void dispatch(ResetFormPositionHandler handler) {
		handler.onResetFormPosition(this);
	}
	
	public int getInsertPosition(){
		return insertPos;
	}

	@Override
	public Type<ResetFormPositionHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ResetFormPositionHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, int insertPos) {
		source.fireEvent(new ResetFormPositionEvent(insertPos));
	}

	public int getCount() {
		return count;
	}

	public void addCount() {
		++count;
	}
}
