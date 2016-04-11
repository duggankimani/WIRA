package com.duggan.workflow.client.reports;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
public class ReportsPresenter extends Presenter<ReportsPresenter.MyView, ReportsPresenter.MyProxy>  {
    interface MyView extends View  {
    }
    
    @NameToken(NameTokens.reports)
    @ProxyStandard
    interface MyProxy extends TabContentProxyPlace<ReportsPresenter> {
    }

	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(Gatekeeper gateKeeper) {
        return new HomeTabData("reports","Report Registry","",10, gateKeeper);
    }
	
    @Inject
    ReportsPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy) {
        super(eventBus, view, proxy, MainPagePresenter.CONTENT_SLOT);
        
    }
    
    
}