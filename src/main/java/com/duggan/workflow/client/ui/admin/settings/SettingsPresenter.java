package com.duggan.workflow.client.ui.admin.settings;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.SaveSettingsRequest;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.duggan.workflow.shared.responses.SaveSettingsResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class SettingsPresenter extends
		Presenter<SettingsPresenter.ISettingsView,SettingsPresenter.MyProxy> {

	public interface ISettingsView extends View {
		public void setValues(List<Setting> settings);
		public List<Setting> getSettings();
		boolean isValid();
		HasClickHandlers getSaveLink();
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.settings)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<SettingsPresenter> {
	}
	
	@TabInfo(container = AdminHomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new TabDataExt("Settings","icon-globe",7, adminGatekeeper);
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
