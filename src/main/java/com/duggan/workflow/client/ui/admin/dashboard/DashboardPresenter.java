package com.duggan.workflow.client.ui.admin.dashboard;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.dashboard.charts.PieChartPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.linegraph.LineGraphPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.table.TableDataPresenter;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.shared.model.dashboard.ChartType;
import com.duggan.workflow.shared.requests.GetDashBoardDataRequest;
import com.duggan.workflow.shared.responses.GetDashBoardDataResponse;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class DashboardPresenter extends
		Presenter<DashboardPresenter.IDashboardView, DashboardPresenter.MyProxy> {

	public interface IDashboardView extends View {

		void setValues(Integer requestCount, Integer activeCount,
				Integer failureCount);
		
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.dashboards)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<DashboardPresenter> {
	}
	
	@TabInfo(container = AdminHomePresenter.class)
    static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
        return new TabDataExt(TABLABEL,"icon-dashboard",1, adminGatekeeper);
    }
	
	private IndirectProvider<PieChartPresenter> pieChartFactory;
	private IndirectProvider<LineGraphPresenter> lineGraphFactory;
	private IndirectProvider<TableDataPresenter> tableFactory;
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> OVERALLTURNAROUND_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> REQUESTSPERDOC_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> LINEGRAPH_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> LONGLASTINGTASKS_SLOT = new Type<RevealContentHandler<?>>();
	public static final String TABLABEL = "Dashboards";
	
	@Inject DispatchAsync requestHelper;
	
	@Inject
	public DashboardPresenter(final EventBus eventBus, final IDashboardView view,MyProxy proxy,
			Provider<PieChartPresenter>pieChartProvider,
			Provider<LineGraphPresenter>lineGraphProvider,
			Provider<TableDataPresenter>tableDataProvider) {
		super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
		pieChartFactory = new StandardProvider<PieChartPresenter>(pieChartProvider);
		lineGraphFactory = new StandardProvider<LineGraphPresenter>(lineGraphProvider);
		tableFactory = new StandardProvider<TableDataPresenter>(tableDataProvider);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadCharts();
	}
	
	private void loadCharts() {
		setInSlot(OVERALLTURNAROUND_SLOT, null);
		setInSlot(REQUESTSPERDOC_SLOT, null);
		setInSlot(LINEGRAPH_SLOT, null);
		requestHelper.execute(new GetDashBoardDataRequest(),
				new TaskServiceCallback<GetDashBoardDataResponse>() {
			@Override
			public void processResult(GetDashBoardDataResponse aResponse) {
				getView().setValues(aResponse.getRequestCount(), aResponse.getActiveCount(), aResponse.getFailureCount());
				loadCharts(aResponse);
			}
		});
		
	}

	protected void loadCharts(final GetDashBoardDataResponse dataResponse) {			
		pieChartFactory.get(new ServiceCallback<PieChartPresenter>() {
			@Override
			public void processResult(PieChartPresenter aResponse) {
				aResponse.setChart(ChartType.AGINGANALYSIS);
				aResponse.setValues(dataResponse.getRequestAging());
				setInSlot(OVERALLTURNAROUND_SLOT, aResponse);
			}
		});
		
		pieChartFactory.get(new ServiceCallback<PieChartPresenter>() {
			@Override
			public void processResult(PieChartPresenter aResponse) {
				aResponse.setChart(ChartType.AVGTURNAROUNDPERDOC);
				aResponse.setValues(dataResponse.getDocumentCounts());
				setInSlot(REQUESTSPERDOC_SLOT, aResponse);
			}
		});
		
		lineGraphFactory.get(new ServiceCallback<LineGraphPresenter>() {
			@Override
			public void processResult(LineGraphPresenter aResponse) {
				setInSlot(LINEGRAPH_SLOT, aResponse);
			}
		});
		
		tableFactory.get(new ServiceCallback<TableDataPresenter>() {
			@Override
			public void processResult(TableDataPresenter aResponse) {
				setInSlot(LONGLASTINGTASKS_SLOT, aResponse);
			}
		});

	}
}
