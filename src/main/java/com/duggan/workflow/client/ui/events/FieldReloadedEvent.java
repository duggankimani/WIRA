package com.duggan.workflow.client.ui.events;

import java.util.List;

import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FieldReloadedEvent extends GwtEvent<FieldReloadedEvent.FieldReloadedHandler> {
    private static Type<FieldReloadedHandler> TYPE = new Type<FieldReloadedHandler>();
    
    public interface FieldReloadedHandler extends EventHandler {
        void onFieldReloaded(FieldReloadedEvent event);
    }
    
    
    private final List<Field> fields;
   
    public FieldReloadedEvent(final List<Field> fields) {
        this.fields = fields;
    }

    public static Type<FieldReloadedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final FieldReloadedHandler handler) {
        handler.onFieldReloaded(this);
    }

    @Override
    public Type<FieldReloadedHandler> getAssociatedType() {
        return TYPE;
    }

	public List<Field> getFields() {
		return fields;
	}
    
}