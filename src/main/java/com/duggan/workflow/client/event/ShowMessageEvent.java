package com.duggan.workflow.client.event;

import com.duggan.workflow.client.ui.AlertType;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ShowMessageEvent extends GwtEvent<ShowMessageEvent.ShowMessageHandler> {
    private static Type<ShowMessageHandler> TYPE = new Type<ShowMessageHandler>();
    
    public interface ShowMessageHandler extends EventHandler {
        void onShowMessage(ShowMessageEvent event);
    }
    
    
    private final String message;
	private AlertType alertType;
	private boolean showDefaultHeading;
   
    public ShowMessageEvent(final AlertType alertType,final String message) {
        this.alertType = alertType;
		this.message = message;
    }
    
    public ShowMessageEvent(final AlertType alertType,final String message, boolean showDefaultHeading) {
        this.alertType = alertType;
		this.message = message;
		this.showDefaultHeading = showDefaultHeading;
    }

    public static Type<ShowMessageHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ShowMessageHandler handler) {
        handler.onShowMessage(this);
    }

    @Override
    public Type<ShowMessageHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }

	public AlertType getAlertType() {
		return alertType;
	}

	public boolean isShowDefaultHeading() {
		return showDefaultHeading;
	}

	public void setShowDefaultHeading(boolean showDefaultHeading) {
		this.showDefaultHeading = showDefaultHeading;
	}
}