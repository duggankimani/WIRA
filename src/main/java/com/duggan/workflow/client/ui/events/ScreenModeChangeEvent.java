package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.client.model.ScreenMode;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ScreenModeChangeEvent extends GwtEvent<ScreenModeChangeEvent.ScreenModeChangeHandler> {
    private static Type<ScreenModeChangeHandler> TYPE = new Type<ScreenModeChangeHandler>();
	private ScreenMode screenMode;
    
    public interface ScreenModeChangeHandler extends EventHandler {
        void onScreenModeChange(ScreenModeChangeEvent event);
    }
    
    public ScreenModeChangeEvent(final ScreenMode screenMode) {
        this.screenMode = screenMode;
    }

    public static Type<ScreenModeChangeHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ScreenModeChangeHandler handler) {
        handler.onScreenModeChange(this);
    }

    @Override
    public Type<ScreenModeChangeHandler> getAssociatedType() {
        return TYPE;
    }

	public ScreenMode getScreenMode() {
		return screenMode;
	}
    
}