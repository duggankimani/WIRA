package com.duggan.workflow.client.ui.admin.processes;

import java.util.List;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.processes.save.ProcessSavePresenter;
import com.duggan.workflow.client.ui.admin.processitem.ProcessItemPresenter;
import com.duggan.workflow.client.ui.events.EditProcessEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.EditProcessEvent.EditProcessHandler;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent.LoadProcessesHandler;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.StartAllProcessesRequest;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.duggan.workflow.shared.responses.StartAllProcessesResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ProcessPresenter extends
		PresenterWidget<ProcessPresenter.IProcessView> implements LoadProcessesHandler,
		EditProcessHandler{

	public interface IProcessView extends View {

		HasClickHandlers getaNewProcess();

		HasClickHandlers getStartAllProcesses();
	}
	
	public static final Object TABLE_SLOT = new Object();
	
	@Inject DispatchAsync requestHelper;
	
	IndirectProvider<ProcessSavePresenter> processFactory;
	IndirectProvider<ProcessItemPresenter> processItemFactory;

	@Inject
	public ProcessPresenter(final EventBus eventBus, final IProcessView view,
			Provider<ProcessSavePresenter> addprocessProvider, Provider<ProcessItemPresenter> columnProvider) {
		super(eventBus, view);
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