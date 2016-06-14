package com.duggan.workflow.client.ui.admin.ds;

import java.util.ArrayList;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.ds.item.DSItemPresenter;
import com.duggan.workflow.client.ui.admin.ds.save.DSSavePresenter;
import com.duggan.workflow.client.ui.events.EditDSConfigEvent;
import com.duggan.workflow.client.ui.events.EditDSConfigEvent.EditDSConfigHandler;
import com.duggan.workflow.client.ui.events.LoadDSConfigsEvent;
import com.duggan.workflow.client.ui.events.LoadDSConfigsEvent.LoadDSConfigsHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.requests.GetDSConfigurationsRequest;
import com.duggan.workflow.shared.requests.GetDSStatusRequest;
import com.duggan.workflow.shared.responses.GetDSConfigurationsResponse;
import com.duggan.workflow.shared.responses.GetDSStatusResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
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

public class DataSourcePresenter extends
		Presenter<DataSourcePresenter.IDataSourceView,DataSourcePresenter.MyProxy> 
		implements LoadDSConfigsHandler, EditDSConfigHandler{

	public interface IDataSourceView extends View {

		HasClickHandlers getNewDatasourceButton();
		HasClickHandlers getTestAllDatasources();
	}
	
	public static final String DATASOURCES_CAN_VIEW_DATASOURCES = "DATASOURCES_CAN_VIEW_DATASOURCES";
	
	@ProxyCodeSplit
	@NameToken(NameTokens.datasources)
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({DATASOURCES_CAN_VIEW_DATASOURCES})
	public interface MyProxy extends TabContentProxyPlace<DataSourcePresenter> {
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
		gateKeeper.withParams(new String[]{DATASOURCES_CAN_VIEW_DATASOURCES});
        return new TabDataExt(TABLABEL,"icon-briefcase",9, gateKeeper);
    }
	
	public static final Object TABLE_SLOT = new Object();

	public static final String TABLABEL = "Data Sources";
	
	@Inject DispatchAsync requestHelper;
	
	IndirectProvider<DSSavePresenter> dsSaveFactory;
	IndirectProvider<DSItemPresenter> dsItemFactory;

	@Inject
	public DataSourcePresenter(final EventBus eventBus, final IDataSourceView view,MyProxy proxy,
			Provider<DSSavePresenter> dsSaveProvider, 
			Provider<DSItemPresenter> dsItemProvider) {
		super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
		dsSaveFactory = new StandardProvider<DSSavePresenter>(dsSaveProvider);
		dsItemFactory = new StandardProvider<DSItemPresenter>(dsItemProvider);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(LoadDSConfigsEvent.TYPE, this);
		addRegisteredHandler(EditDSConfigEvent.TYPE, this);
		
		getView().getNewDatasourceButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showConfigSavePopup();
			}
		});
		
		getView().getTestAllDatasources().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent("Starting processes"));
				requestHelper.execute(new GetDSStatusRequest(), 
						new TaskServiceCallback<GetDSStatusResponse>() {
					@Override
					public void processResult(
							GetDSStatusResponse aResponse) {
						bindValues(aResponse.getConfigs());
						fireEvent(new ProcessingCompletedEvent());
					}

				});
			}
		});
		
	}
	
	private void showConfigSavePopup(){
		showConfigSavePopup(null);
	}
	private void showConfigSavePopup(final DSConfiguration config) {
		
		dsSaveFactory.get(new ServiceCallback<DSSavePresenter>() {
			@Override
			public void processResult(DSSavePresenter aResponse) {
				aResponse.setConfiguration(config);
				addToPopupSlot(aResponse,false);
			}
		});
			
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		loadConfigurations();
	}

	public void loadConfigurations(){
		
		fireEvent(new ProcessingEvent());
		requestHelper.execute(new GetDSConfigurationsRequest(),
				new TaskServiceCallback<GetDSConfigurationsResponse>() {
			@Override
			public void processResult(GetDSConfigurationsResponse result) {
				ArrayList<DSConfiguration> configs = result.getConfigurations();
				bindValues(configs);
				fireEvent(new ProcessingCompletedEvent());
				
			}
		});
	}
	
	private void bindValues(ArrayList<DSConfiguration> configs) {
		setInSlot(TABLE_SLOT, null);
		if(configs!=null){
			for(final DSConfiguration config: configs){
				dsItemFactory.get(new ServiceCallback<DSItemPresenter>() {
					@Override
					public void processResult(
							DSItemPresenter result) {
						result.setConfiguration(config);
						addToSlot(TABLE_SLOT, result);
					}
				});
			}
		}
	}


	@Override
	public void onLoadDSConfigs(LoadDSConfigsEvent event) {
		loadConfigurations();
	}

	@Override
	public void onEditDSConfig(EditDSConfigEvent event) {
		showConfigSavePopup(event.getConfiguration());
	}
}