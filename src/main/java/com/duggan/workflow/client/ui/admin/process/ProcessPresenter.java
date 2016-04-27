package com.duggan.workflow.client.ui.admin.process;

import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.processes.save.ProcessSavePresenter;
import com.duggan.workflow.client.ui.admin.processitem.ProcessItemPresenter;
import com.duggan.workflow.client.ui.admin.processitem.ProcessStepsPresenter;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.events.ProcessSelectedEvent;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ProcessPresenter extends
		Presenter<ProcessPresenter.IProcessView, ProcessPresenter.MyProxy>{

	public interface IProcessView extends View {
		void setProcess(ProcessDef processDef);

		void clear();

		void setConfig(String actionPreview);

	}

	public static final String ACTION_PREVIEW = "preview";
	public static final String ACTION_CONFIG = "config";
	
	@Inject
	DispatchAsync requestHelper;
	
	public static final Object TABLE_SLOT = new Object();
	
	@Inject PlaceManager placeManager;

	IndirectProvider<ProcessStepsPresenter> taskStepsFactory;

	@ProxyCodeSplit
	@NameToken(NameTokens.processes)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<ProcessPresenter> {
	}

	@Inject
	public ProcessPresenter(final EventBus eventBus, final IProcessView view,
			final MyProxy proxy,
			Provider<ProcessSavePresenter> addprocessProvider,
			Provider<ProcessItemPresenter> columnProvider,
			Provider<ProcessStepsPresenter> taskStepsProvider) {
		super(eventBus, view, proxy, BaseProcessPresenter.CONTENT_SLOT);
		taskStepsFactory = new StandardProvider<ProcessStepsPresenter>(
				taskStepsProvider);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		String action = request.getParameter("a", null);
		String processRefId = request.getParameter("p", null);
		getView().clear();
		if(action!=null && processRefId!=null){
			
			if(action.equals(ACTION_PREVIEW)){
				getView().setConfig(ACTION_PREVIEW);
				fireEvent(new ProcessChildLoadedEvent(this, ACTION_PREVIEW));
			}else{
				getView().setConfig(ACTION_CONFIG);
				fireEvent(new ProcessChildLoadedEvent(this, ACTION_CONFIG));
			}
			loadProcess(processRefId);
		}
		
	}

	private void loadProcess(String processRefId) {
		GetProcessRequest request  = new GetProcessRequest(processRefId);
		requestHelper.execute(request, new TaskServiceCallback<GetProcessResponse>() {
			@Override
			public void processResult(GetProcessResponse aResponse) {
				final ProcessDef processDef = aResponse.getProcessDef();
				getView().setProcess(processDef);
				fireEvent(new ProcessSelectedEvent(processDef, true));

				// Task Steps
				taskStepsFactory
						.get(new ServiceCallback<ProcessStepsPresenter>() {
							@Override
							public void processResult(
									ProcessStepsPresenter aResponse) {
								aResponse.setProcess(processDef);
								aResponse.load();
								setInSlot(TABLE_SLOT, aResponse);
							}
						});
			}
		});
		
	}

}