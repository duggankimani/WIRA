package com.duggan.workflow.client.ui.events;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FieldReloadedEvent extends GwtEvent<FieldReloadedEvent.FieldReloadedHandler> {
    private static Type<FieldReloadedHandler> TYPE = new Type<FieldReloadedHandler>();
    
    public interface FieldReloadedHandler extends EventHandler {
        void onFieldReloaded(FieldReloadedEvent event);
    }
    
    private boolean isFormReadOnly;
    private final ArrayList<Field> fields;
   
    public FieldReloadedEvent(final ArrayList<Field> fields, boolean isFormReadOnly) {
        this.fields = fields;
		this.isFormReadOnly = isFormReadOnly;
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

	public ArrayList<Field> getFields() {
		return fields;
	}

	public boolean isFormReadOnly() {
		return isFormReadOnly;
	}

	public void setFormReadOnly(boolean isFormReadOnly) {
		this.isFormReadOnly = isFormReadOnly;
	}
    
}