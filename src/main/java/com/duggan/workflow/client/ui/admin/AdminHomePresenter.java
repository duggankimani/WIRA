package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ChangeTabHandler;
import com.gwtplatform.mvp.client.RequestTabsHandler;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabContainerPresenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabView;
import com.gwtplatform.mvp.client.annotations.ChangeTab;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.RequestTabs;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class AdminHomePresenter extends
	TabContainerPresenter<AdminHomePresenter.MyView, AdminHomePresenter.MyProxy>{

	public interface MyView extends TabView {		
		public void refreshTabs();
		void changeTab(Tab tab, TabData tabData, String historyToken);
	}
	
	@ProxyStandard
	public interface MyProxy extends Proxy<AdminHomePresenter> {
	}

	/**
     * This will be the event sent to our "unknown" child presenters, in order for them to register their tabs.
     */
    @RequestTabs
    public static final Type<RequestTabsHandler> SLOT_RequestTabs = new Type<RequestTabsHandler>();

    /**
     * Fired by child proxie's when their tab content is changed.
     */
    @ChangeTab
    public static final Type<ChangeTabHandler> SLOT_ChangeTab = new Type<ChangeTabHandler>();

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_SetTabContent = new Type<RevealContentHandler<?>>();
    
	@Inject
	public AdminHomePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy,SLOT_SetTabContent,SLOT_RequestTabs, SLOT_ChangeTab,MainPagePresenter.CONTENT_SLOT);		
	}
	
	@ProxyEvent
	public void onContextLoaded(ContextLoadedEvent event){
		getView().refreshTabs();
	}
	
	
}
