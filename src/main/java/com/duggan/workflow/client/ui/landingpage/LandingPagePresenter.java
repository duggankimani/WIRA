package com.duggan.workflow.client.ui.landingpage;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
public class LandingPagePresenter extends Presenter<LandingPagePresenter.ILandingPageView, LandingPagePresenter.ILandingPageProxy>  {
    interface ILandingPageView extends View  {
    }
    
    @ProxyCodeSplit
    @NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ILandingPageProxy extends TabContentProxyPlace<LandingPagePresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper gatekeeper) {
		HomeTabData data = new HomeTabData("landing","Landing","icon-dashboard",11,gatekeeper, false);
        return data;
    }

    @Inject
    LandingPagePresenter(
            EventBus eventBus,
            ILandingPageView view, 	
            ILandingPageProxy proxy) {
        super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
        
    }
    
    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
    }
    
    protected void onBind() {
        super.onBind();
    }
    
}