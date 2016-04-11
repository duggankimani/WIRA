package com.duggan.workflow.client.reports;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
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
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ReportsPresenter extends
		Presenter<ReportsPresenter.IReportsView, ReportsPresenter.IReportsProxy> {
	interface IReportsView extends View {

		void bindCatalogs(List<Catalog> catalogs);

		void bind(Catalog catalog, List<DocumentLine> data);
	}

	@NameToken(NameTokens.reports)
	@ProxyStandard
	interface IReportsProxy extends TabContentProxyPlace<ReportsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper gateKeeper) {
		return new HomeTabData("reports", "Report Registry", "", 10, gateKeeper);
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	ReportsPresenter(EventBus eventBus, IReportsView view, IReportsProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		String catalogRefId = request.getParameter("reportRefId", null);
		loadData(catalogRefId);
	}

	private void loadData(final String catalogRefId) {
		fireEvent(new ProcessingEvent("Loading..."));
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetCatalogsRequest(catalogRefId));
		if(catalogRefId!=null){
			action.addRequest(new GetDataRequest(catalogRefId));
		}
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						int i=0;
						
						
						GetCatalogsResponse getCatalogs =  (GetCatalogsResponse) aResponse.get(0);
						List<Catalog> catalogs = getCatalogs.getCatalogs();
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

}