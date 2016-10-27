package com.duggan.workflow.shared.event;

import com.duggan.workflow.client.ui.document.VIEWS;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ShowViewEvent extends GwtEvent<ShowViewEvent.ShowViewHandler> {
    private static Type<ShowViewHandler> TYPE = new Type<ShowViewHandler>();
    
    public interface ShowViewHandler extends EventHandler {
        void onShowView(ShowViewEvent event);
    }
    
    
    private final VIEWS view;

	public ShowViewEvent(final VIEWS view) {
		this.view = view;
        
    }

    public static Type<ShowViewHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ShowViewHandler handler) {
        handler.onShowView(this);
    }

    @Override
    public Type<ShowViewHandler> getAssociatedType() {
        return TYPE;
    }
    
    public VIEWS getView() {
		return view;
	}

    
}