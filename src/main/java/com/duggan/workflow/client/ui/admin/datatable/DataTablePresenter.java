package com.duggan.workflow.client.ui.admin.datatable;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
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
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
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
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class DataTablePresenter extends
		Presenter<DataTablePresenter.IDataTableView,DataTablePresenter.IDataTableProxy> 
		implements LoadDSConfigsHandler, EditDSConfigHandler{

	public interface IDataTableView extends View {

		HasClickHandlers getNewDatasourceButton();
		HasClickHandlers getTestAllDatasources();
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.datatable)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface IDataTableProxy extends TabContentProxyPlace<DataTablePresenter> {
	}
	
	@TabInfo(container = AdminHomePresenter.class)
    static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt ext = new TabDataExt("Data Table","icon-th",8, adminGatekeeper);
        return ext;
    }
	
	public static final Object TABLE_SLOT = new Object();
	
	@Inject DispatchAsync requestHelper;

	@Inject
	public DataTablePresenter(final EventBus eventBus, final IDataTableView view,IDataTableProxy proxy,
			Provider<DSSavePresenter> dsSaveProvider, 
			Provider<DSItemPresenter> dsItemProvider) {
		super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
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
				List<DSConfiguration> configs = result.getConfigurations();
				bindValues(configs);
				fireEvent(new ProcessingCompletedEvent());
				
			}
		});
	}
	
	private void bindValues(List<DSConfiguration> configs) {
		setInSlot(TABLE_SLOT, null);
		if(configs!=null){
			for(final DSConfiguration config: configs){
				
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