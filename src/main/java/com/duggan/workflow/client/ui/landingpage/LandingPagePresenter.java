package com.duggan.workflow.client.ui.landingpage;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
public class LandingPagePresenter extends Presenter<LandingPagePresenter.MyView, LandingPagePresenter.ILandingPageProxy>  {
    interface MyView extends View  {
    }
    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_LandingPage = new Type<RevealContentHandler<?>>();
    
	@ProxyCodeSplit
	@NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ILandingPageProxy extends TabContentProxyPlace<LandingPagePresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Landing","glyphicon glyphicon-landing",10, adminGatekeeper,false);
        return data;
    }


    @Inject
    LandingPagePresenter(
            EventBus eventBus,
            MyView view, 
            ILandingPageProxy proxy) {
    	super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
        
    }
    
    
}