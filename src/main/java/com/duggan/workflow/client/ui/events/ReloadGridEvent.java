package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ReloadGridEvent extends GwtEvent<ReloadGridEvent.ReloadGridHandler> {
    private static Type<ReloadGridHandler> TYPE = new Type<ReloadGridHandler>();
    
    public interface ReloadGridHandler extends EventHandler {
        void onReloadGrid(ReloadGridEvent event);
    }
    
    
    private final Field field;
   
    public ReloadGridEvent(final Field field) {
        this.field = field;
    }

    public static Type<ReloadGridHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ReloadGridHandler handler) {
        handler.onReloadGrid(this);
    }

    @Override
    public Type<ReloadGridHandler> getAssociatedType() {
        return TYPE;
    }
    
	public Field getField() {
		return field;
	}
}