package com.duggan.workflow.client.ui.admin.settings;

import java.util.ArrayList;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.SaveSettingsRequest;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.duggan.workflow.shared.responses.SaveSettingsResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class SettingsPresenter extends
		Presenter<SettingsPresenter.ISettingsView,SettingsPresenter.MyProxy> {

	public interface ISettingsView extends View {
		public void setValues(ArrayList<Setting> settings);
		public ArrayList<Setting> getSettings();
		boolean isValid();
		HasClickHandlers getSaveLink();
	}
	
	public static final String SETTINGS_CAN_VIEW = "SETTINGS_CAN_VIEW";
	
	@ProxyCodeSplit
	@NameToken(NameTokens.settings)
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({SETTINGS_CAN_VIEW})
	public interface MyProxy extends TabContentProxyPlace<SettingsPresenter> {
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
		gateKeeper.withParams(new String[]{SETTINGS_CAN_VIEW}); 
        return new TabDataExt("Settings","icon-globe",7, gateKeeper);
    }
	
	
	@Inject DispatchAsync requestHelper;

	@Inject
	public SettingsPresenter(final EventBus eventBus, final ISettingsView view, MyProxy proxy) {
		super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getSaveLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					
					requestHelper.execute(new SaveSettingsRequest(getView().getSettings()),
							new TaskServiceCallback<SaveSettingsResponse>() {
						@Override
						public void processResult(
								SaveSettingsResponse aResponse) {
							getView().setValues(aResponse.getSettings());
							AppContext.reloadContext();
						}
					}); 
				}
			}
		});
		
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		loadSettings();
	}

	boolean loaded = false;
	private void loadSettings() {
		if(loaded){
			return;
		}
		loaded=true;
		requestHelper.execute(new GetSettingsRequest(), new TaskServiceCallback<GetSettingsResponse>() {
			@Override
			public void processResult(GetSettingsResponse aResponse) {
				getView().setValues(aResponse.getSettings());
			}
		});
	}
}
