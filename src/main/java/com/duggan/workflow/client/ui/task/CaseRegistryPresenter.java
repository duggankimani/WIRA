package com.duggan.workflow.client.ui.task;

import java.util.ArrayList;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.client.ui.util.StringUtils;
import com.duggan.workflow.shared.events.ProcessingCompletedEvent;
import com.duggan.workflow.shared.events.ProcessingEvent;
import com.duggan.workflow.shared.events.SearchEvent;
import com.duggan.workflow.shared.events.SearchEvent.SearchHandler;
import com.duggan.workflow.shared.model.CaseFilter;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessLog;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.requests.GetProcessInstancesRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.duggan.workflow.shared.responses.GetProcessInstancesResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.google.inject.Provider;
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
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.shared.models.HTUser;

public class CaseRegistryPresenter
		extends
		Presenter<CaseRegistryPresenter.ICaseRegistryView, CaseRegistryPresenter.ICaseRegistryProxy>
		implements SearchHandler {

	public interface ICaseRegistryView extends View {
		void bindProcesses(ArrayList<DocumentType> documentTypes);

		void bindProcessInstances(ArrayList<ProcessLog> logs);

		void onReveal();

		void bindUsers(ArrayList<HTUser> users);

		public Anchor getSearch();

		CaseFilter getCaseFilter();

		TextField getTxtCaseNo();
	}
	
	public static final String CASEREGISTRY_CAN_VIEW_CASES = "CASEREGISTRY_CAN_VIEW_CASES";

	@ProxyCodeSplit
	@NameToken({ NameTokens.registry,NameTokens.registryview })
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({CASEREGISTRY_CAN_VIEW_CASES})
	public interface ICaseRegistryProxy extends
			TabContentProxyPlace<CaseRegistryPresenter> {
	}

	public static final String TABLABEL = "Case Registry";

	@TabInfo(container = HomePresenter.class)
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
		gateKeeper.withParams(new String[]{CASEREGISTRY_CAN_VIEW_CASES});
		return new HomeTabData("registry", TABLABEL, "", 6,
				gateKeeper, false);
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public CaseRegistryPresenter(EventBus eventBus, CaseRegistryView view,
			ICaseRegistryProxy proxy,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SearchEvent.getType(), this);
		getView().getSearch().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadData(getView().getCaseFilter());
			}
		});

		getView().getTxtCaseNo().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					loadData(getView().getCaseFilter());
				}
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadData();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().onReveal();
	}

	private void loadData(CaseFilter filter) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProcessInstancesRequest(filter));
		fireEvent(new ProcessingEvent());
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i = 0;
						GetProcessInstancesResponse response = (GetProcessInstancesResponse) aResponse
								.get(i++);
						getView().bindProcessInstances(response.getLogs());
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	private void loadData() {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetDocumentTypesRequest());
		action.addRequest(new GetProcessInstancesRequest(null));
		action.addRequest(new GetUsersRequest());
		fireEvent(new ProcessingEvent());
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i = 0;
						getView().bindProcesses(
								((GetDocumentTypesResponse) aResponse.get(i++))
										.getDocumentTypes());

						GetProcessInstancesResponse response = (GetProcessInstancesResponse) aResponse
								.get(i++);
						getView().bindProcessInstances(response.getLogs());

						GetUsersResponse getUsersReponse = (GetUsersResponse) aResponse
								.get(i++);
						getView().bindUsers(getUsersReponse.getUsers());

						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	@Override
	public void onSearch(SearchEvent event) {
		if (this.isVisible()) {
			CaseFilter filter = new CaseFilter();
			filter.setCaseNo(event.getFilter().getPhrase());

			if (StringUtils.isNullOrEmpty(filter.getCaseNo())) {
				loadData(null);
			} else {
				loadData(filter);
			}
		}
	}
}
