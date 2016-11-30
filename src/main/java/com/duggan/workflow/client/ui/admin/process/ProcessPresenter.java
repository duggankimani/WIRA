package com.duggan.workflow.client.ui.admin.process;

import java.util.ArrayList;

import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.processitem.ProcessStepsPresenter;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.events.ProcessSelectedEvent;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
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
import com.gwtplatform.mvp.client.presenter.slots.SingleSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.client.service.ServiceCallback;

public class ProcessPresenter extends
		Presenter<ProcessPresenter.IProcessView, ProcessPresenter.MyProxy>{

	public interface IProcessView extends View {
		void setProcess(ProcessDef processDef);

		void clear();

		void setConfig(String actionPreview);

		void bindWorkbench(String org_unit, String org_repo,
				ProcessDef processDef);

	}

	public static final String ACTION_PREVIEW = "preview";
	public static final String ACTION_CONFIG = "config";
	
	@Inject
	DispatchAsync requestHelper;
	
	public static final SingleSlot<ProcessStepsPresenter> TABLE_SLOT = new SingleSlot<ProcessStepsPresenter>();
	
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
		
		MultiRequestAction action = new MultiRequestAction();
		GetProcessRequest request  = new GetProcessRequest(processRefId);
		action.addRequest(request);
		
		ArrayList<SETTINGNAME> names = new ArrayList<SETTINGNAME>();
		names.add(SETTINGNAME.ORG_REPO);
		names.add(SETTINGNAME.ORG_UNIT);
		GetSettingsRequest settingsRequest = new GetSettingsRequest(names);
		action.addRequest(settingsRequest);
		
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetProcessResponse processResp = (GetProcessResponse) aResponse.get(0);
				final ProcessDef processDef = processResp.getProcessDef();
				getView().setProcess(processDef);
				
				GetSettingsResponse getSettings = (GetSettingsResponse) aResponse.get(1);
				ArrayList<Setting> settings = getSettings.getSettings();
				String org_repo = null;
				String org_unit = null;
				if(settings.get(0).getName() == SETTINGNAME.ORG_REPO){
					Value orgRepo =  settings.get(0).getValue();
					Value orgUnit = settings.get(1).getValue();
					
					org_repo  = (orgRepo==null || orgRepo.getValue()==null)? null : orgRepo.getValue().toString(); 
					org_unit  = (orgUnit==null || orgUnit.getValue()==null)? null : orgUnit.getValue().toString();
				}else{
					Value orgRepo =  settings.get(1).getValue();
					Value orgUnit = settings.get(0).getValue();
					
					org_repo  = (orgRepo==null || orgRepo.getValue()==null)? null : orgRepo.getValue().toString(); 
					org_unit  = (orgUnit==null || orgUnit.getValue()==null)? null : orgUnit.getValue().toString();
				}
				//Bind workbench view
				getView().bindWorkbench(org_unit, org_repo, processDef);
				
				
				fireEvent(new ProcessSelectedEvent(processDef, true));

				// Task Steps
				taskStepsFactory
						.get(new ServiceCallback<ProcessStepsPresenter>() {
							@Override
							public void processResult(
									ProcessStepsPresenter aResponse) {
								setInSlot(TABLE_SLOT, aResponse);
								aResponse.setProcess(processDef);
								aResponse.load();
							}
						});
			}
		});
		
	}
	
}