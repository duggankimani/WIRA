package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SetValueEvent extends GwtEvent<SetValueEvent.SetValueHandler> {
    private static Type<SetValueHandler> TYPE = new Type<SetValueHandler>();
    
    public interface SetValueHandler extends EventHandler {
        void onSetValue(SetValueEvent event);
    }
    
    
    private String key;
	private String value;
   
    public SetValueEvent(String key, String value) {
		this.key = key;
		this.value = value;
    }

    public static Type<SetValueHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final SetValueHandler handler) {
        handler.onSetValue(this);
    }

    @Override
    public Type<SetValueHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
    
}