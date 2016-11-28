package com.duggan.workflow.client.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.shared.events.ProcessingCompletedEvent;
import com.duggan.workflow.shared.events.ProcessingEvent;
import com.duggan.workflow.shared.events.SearchEvent;
import com.duggan.workflow.shared.events.SearchEvent.SearchHandler;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.requests.GetCatalogsRequest;
import com.duggan.workflow.shared.requests.GetDataRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;
import com.duggan.workflow.shared.responses.GetDataResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
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
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ReportsPresenter extends
		Presenter<ReportsPresenter.IReportsView, ReportsPresenter.IReportsProxy> 
implements SearchHandler{
	public interface IReportsView extends View {

		void bindCatalogs(ArrayList<Catalog> catalogs);

		void bind(Catalog catalog, ArrayList<DocumentLine> data);
	}
	
	public static final String REPORTS_CAN_VIEW_REPORTS = "REPORTS_CAN_VIEW_REPORTS";

	@NameToken({NameTokens.reports,NameTokens.reportsview})
	@ProxyCodeSplit
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({REPORTS_CAN_VIEW_REPORTS})
	public interface IReportsProxy extends TabContentProxyPlace<ReportsPresenter> {
	}

	public static final String TABLABEL = "Report Registry";

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
		gateKeeper.withParams(new String[]{REPORTS_CAN_VIEW_REPORTS});
		return new HomeTabData("reports",TABLABEL , "", 10, gateKeeper,false);
	}

	@Inject
	DispatchAsync requestHelper;
	String catalogRefId;

	@Inject
	public ReportsPresenter(EventBus eventBus, IReportsView view, IReportsProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);

	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SearchEvent.getType(), this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		catalogRefId = request.getParameter("reportRefId", null);
		loadData(null,catalogRefId);
	}

	private void loadData(String searchTerm,final String catalogRefId) {
		fireEvent(new ProcessingEvent("Loading..."));
		MultiRequestAction action = new MultiRequestAction();
		
		GetCatalogsRequest req = new GetCatalogsRequest(catalogRefId);
		req.setSearchTerm(searchTerm);
		
		action.addRequest(req);
		if(catalogRefId!=null){
			GetDataRequest getDataReq = new GetDataRequest(catalogRefId);
			getDataReq.setSearchTerm(searchTerm);
			action.addRequest(getDataReq);
		}
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i=0;
						
						
						GetCatalogsResponse getCatalogs =  (GetCatalogsResponse) aResponse.get(0);
						ArrayList<Catalog> catalogs = getCatalogs.getCatalogs();
						if (catalogRefId != null && !catalogs.isEmpty()) {
							GetDataResponse getData = (GetDataResponse) aResponse.get(1);
							getView().bind(catalogs.get(0), getData.getLines());
						} else {

							Collections.sort(catalogs,
									new Comparator<Catalog>() {
										@Override
										public int compare(Catalog pojo1,
												Catalog pojo2) {
											if (pojo1.getCategory() == null)
												return -1;

											if (pojo2.getCategory() == null)
												return 1;

											return pojo1
													.getCategory()
													.getName()
													.compareTo(
															pojo2.getCategory()
																	.getName());
										}
									});
							getView().bindCatalogs(catalogs);
						}
						
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}
	
	@Override
	public void onSearch(SearchEvent event) {
		if(isVisible()){
			loadData(event.getFilter().getPhrase(),catalogRefId);
		}
	}

}