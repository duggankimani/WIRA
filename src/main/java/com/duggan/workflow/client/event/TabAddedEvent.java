package com.duggan.workflow.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.gwtplatform.mvp.client.Tab;

public class TabAddedEvent extends GwtEvent<TabAddedEvent.TabAddedHandler> {
    private static Type<TabAddedHandler> TYPE = new Type<TabAddedHandler>();
	private Tab tab;
    
    public interface TabAddedHandler extends EventHandler {
        void onTabAdded(TabAddedEvent event);
    }
    
    public TabAddedEvent(Tab tab) {
		this.tab = tab;
        
    }

    public static Type<TabAddedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final TabAddedHandler handler) {
        handler.onTabAdded(this);
    }

    @Override
    public Type<TabAddedHandler> getAssociatedType() {
        return TYPE;
    }

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}
    
}