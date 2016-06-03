package com.duggan.workflow.client.ui.admin.processmgt;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class BaseProcessPresenter extends
		Presenter<BaseProcessPresenter.MyView, BaseProcessPresenter.MyProxy> {
	interface MyView extends View {
	}

	public static final NestedSlot CONTENT_SLOT = new NestedSlot();

	@Inject
	PlaceManager placeManager;

	public static final String CAN_VIEW_PROCESSES = "PROCESSES_CAN_VIEW_PROCESSES";
	
	@ProxyCodeSplit
	@NameToken(NameTokens.processconf)
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({CAN_VIEW_PROCESSES})
	public interface MyProxy extends TabContentProxyPlace<BaseProcessPresenter> {
	}

	@TabInfo(container = AdminHomePresenter.class)
	static TabData getTabLabel(HasPermissionsGateKeeper gateKeeper) {
		/**
		 * Manually calling gateKeeper.withParams Method.
		 * 
		 * HACK NECESSITATED BY THE FACT THAT Gin injects to different instances of this GateKeeper in 
		 * Presenter.MyProxy->UseGateKeeper & 
		 * getTabLabel(GateKeeper);
		 * 
		 * Test -> 
		 * Window.alert in GateKeeper.canReveal(this+" Params = "+params) Vs 
		 * Window.alert here in getTabLabel.canReveal(this+" Params = "+params) Vs
		 * Window.alert in AbstractTabPanel.refreshTabs(tab.getTabData.getGateKeeper()+" Params = "+params) Vs
		 * 
		 */
		gateKeeper.withParams(new String[]{CAN_VIEW_PROCESSES}); 
		return new TabDataExt(TABLABEL, "icon-cogs", 2, gateKeeper);
	}

	public static final String TABLABEL = "Processes";

	@Inject
	BaseProcessPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		placeManager.revealPlace(new PlaceRequest.Builder().nameToken(
				NameTokens.processlist).build());
	}

}