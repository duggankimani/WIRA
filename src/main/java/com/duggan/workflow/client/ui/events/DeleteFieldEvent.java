package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DeleteFieldEvent extends GwtEvent<DeleteFieldEvent.DeleteFieldHandler> {
    private static Type<DeleteFieldHandler> TYPE = new Type<DeleteFieldHandler>();
    
    public interface DeleteFieldHandler extends EventHandler {
        void onDeleteField(DeleteFieldEvent event);
    }
    
    
    private final Field field;
   
    public DeleteFieldEvent(final Field field) {
        this.field = field;
    }

    public static Type<DeleteFieldHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final DeleteFieldHandler handler) {
        handler.onDeleteField(this);
    }

    @Override
    public Type<DeleteFieldHandler> getAssociatedType() {
        return TYPE;
    }

	public Field getField() {
		return field;
	}
}