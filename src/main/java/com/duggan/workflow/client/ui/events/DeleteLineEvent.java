package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DeleteLineEvent extends GwtEvent<DeleteLineEvent.DeleteLineHandler> {
    private static Type<DeleteLineHandler> TYPE = new Type<DeleteLineHandler>();
    
    public interface DeleteLineHandler extends EventHandler {
        void onDeleteLine(DeleteLineEvent event);
    }
    
	private String gridName;
	private String tempId;
   
    public DeleteLineEvent(final String gridName, final String tempId) {
		this.gridName = gridName;
		this.tempId = tempId;
    }

    public static Type<DeleteLineHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final DeleteLineHandler handler) {
        handler.onDeleteLine(this);
    }

    @Override
    public Type<DeleteLineHandler> getAssociatedType() {
        return TYPE;
    }

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
    
}