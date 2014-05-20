package com.duggan.workflow.client.ui.admin.processes;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.processes.save.ProcessSavePresenter;
import com.duggan.workflow.client.ui.admin.processitem.ProcessItemPresenter;
import com.duggan.workflow.client.ui.events.EditProcessEvent;
import com.duggan.workflow.client.ui.events.EditProcessEvent.EditProcessHandler;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent.LoadProcessesHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.StartAllProcessesRequest;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.duggan.workflow.shared.responses.StartAllProcessesResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ProcessPresenter extends
		Presenter<ProcessPresenter.IProcessView, ProcessPresenter.MyProxy> implements LoadProcessesHandler,
		EditProcessHandler{

	public interface IProcessView extends View {

		HasClickHandlers getaNewProcess();

		HasClickHandlers getStartAllProcesses();
	}
	
	public static final Object TABLE_SLOT = new Object();
	
	@Inject DispatchAsync requestHelper;
	
	IndirectProvider<ProcessSavePresenter> processFactory;
	IndirectProvider<ProcessItemPresenter> processItemFactory;
	
	@ProxyCodeSplit
	@NameToken(NameTokens.processes)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<ProcessPresenter> {
	}
	
	@TabInfo(container = AdminHomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new TabDataExt("Processes","icon-cogs",2, adminGatekeeper);
    }

	@Inject
	public ProcessPresenter(final EventBus eventBus, final IProcessView view,final MyProxy proxy,
			Provider<ProcessSavePresenter> addprocessProvider, Provider<ProcessItemPresenter> columnProvider) {
		super(eventBus,view,proxy,AdminHomePresenter.SLOT_SetTabContent);
		processFactory = new StandardProvider<ProcessSavePresenter>(addprocessProvider);
		processItemFactory= new StandardProvider<ProcessItemPresenter>(columnProvider);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(LoadProcessesEvent.TYPE, this);
		addRegisteredHandler(EditProcessEvent.TYPE, this);
		
		getView().getaNewProcess().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showAddProcessPopup();
			}
		});
		
		getView().getStartAllProcesses().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent("Starting processes"));
				requestHelper.execute(new StartAllProcessesRequest(), 
						new TaskServiceCallback<StartAllProcessesResponse>() {
					@Override
					public void processResult(
							StartAllProcessesResponse aResponse) {
						loadProcesses();
					}
				});
			}
		});
		
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		// TODO Auto-generated method stub
		super.prepareFromRequest(request);
		loadProcesses();
	}
	
	private void showAddProcessPopup(){
		showAddProcessPopup(null);
	}
	private void showAddProcessPopup(final Long processDefId) {
		processFactory.get(new ServiceCallback<ProcessSavePresenter>() {
			@Override
			public void processResult(ProcessSavePresenter result) {
				result.setProcessDefId(processDefId);
				addToPopupSlot(result,false);
			}
		});
			
	}

	public void loadProcesses(){
		
		fireEvent(new ProcessingEvent());
		requestHelper.execute(new GetProcessesRequest(),new TaskServiceCallback<GetProcessesResponse>() {
			@Override
			public void processResult(GetProcessesResponse result) {
				List<ProcessDef> processDefinitions = result.getProcesses();
				setInSlot(TABLE_SLOT, null);
				
				if(processDefinitions!=null){
					for(final ProcessDef def: processDefinitions){
						processItemFactory.get(new ServiceCallback<ProcessItemPresenter>() {
							@Override
							public void processResult(
									ProcessItemPresenter result) {
								
								result.setProcess(def);
								addToSlot(TABLE_SLOT, result);
								
							}
						});
					}
				}
				
				fireEvent(new ProcessingCompletedEvent());
				
			}
		});
	}

	@Override
	public void onLoadProcesses(LoadProcessesEvent event) {
		loadProcesses();
	}
	
	@Override
	public void onEditProcess(EditProcessEvent event) {
		Long processDefId = event.getProcessId();
		showAddProcessPopup(processDefId);
	}
	
}