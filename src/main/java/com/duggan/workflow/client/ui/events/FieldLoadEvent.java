package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FieldLoadEvent extends GwtEvent<FieldLoadEvent.FieldLoadHandler> {
    private static Type<FieldLoadHandler> TYPE = new Type<FieldLoadHandler>();
    
    public interface FieldLoadHandler extends EventHandler {
        void onFieldLoad(FieldLoadEvent event);
    }
    
    private final Field field;
	private String triggerName;
   
    public FieldLoadEvent(final Field field, String triggerName) {
        this.field = field;
		this.triggerName = triggerName;
    }

    public static Type<FieldLoadHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final FieldLoadHandler handler) {
        handler.onFieldLoad(this);
    }

    @Override
    public Type<FieldLoadHandler> getAssociatedType() {
        return TYPE;
    }

	public Field getField() {
		return field;
	}

	public String getTriggerName() {
		return triggerName;
	}

}