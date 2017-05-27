package com.duggan.workflow.client.ui.admin.dashboard;

import java.util.ArrayList;
import java.util.Date;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.dashboard.EmployeeWorkload;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.model.dashboard.ProcessTrend;
import com.duggan.workflow.shared.model.dashboard.ProcessesSummary;
import com.duggan.workflow.shared.model.dashboard.TaskAging;
import com.duggan.workflow.shared.requests.GetDashboardDataRequest;
import com.duggan.workflow.shared.requests.GetDashboardProcessTrendsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetDashboardDataResponse;
import com.duggan.workflow.shared.responses.GetDashboardProcessTrendsResponse;
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

public class DashboardPresenter extends
		Presenter<DashboardPresenter.IDashboardView, DashboardPresenter.MyProxy> {

	public interface IDashboardView extends View {

		void setDashboardData(String processRefId, ArrayList<EmployeeWorkload> workloads,
				ArrayList<ProcessesSummary> processesSummary);

		void setProcessAgingData(ArrayList<TaskAging> aging,
				ArrayList<LongTask> longTasks);

		void setProcessTrendsData(ArrayList<ProcessTrend> startTrend,
				ArrayList<ProcessTrend> completionTrend);
		
	}
	
	public static final String DASHBOARDS_CAN_VIEW_DASHBOARDS = "DASHBOARDS_CAN_VIEW_DASHBOARDS";
	
	@ProxyCodeSplit
	@NameToken({NameTokens.dashboards, NameTokens.dashboardsPerProcess})
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({DASHBOARDS_CAN_VIEW_DASHBOARDS})
	public interface MyProxy extends TabContentProxyPlace<DashboardPresenter> {
	}
	
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
		gateKeeper.withParams(new String[]{DASHBOARDS_CAN_VIEW_DASHBOARDS}); 
        return new TabDataExt(TABLABEL,"icon-dashboard",1, gateKeeper);
    }
	
	public static final String TABLABEL = "Dashboards";
	
	@Inject DispatchAsync requestHelper;
	
	@Inject
	public DashboardPresenter(final EventBus eventBus, final IDashboardView view,MyProxy proxy) {
		super(eventBus, view, proxy,HomePresenter.SLOT_SetTabContent);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		String processRefId = request.getParameter("processRefId", null);
		loadCharts(processRefId);
	}
	
	private void loadCharts(final String processRefId) {
		Date startDate = DateUtils.addDays(new Date(), -365);
		Date endDate = new Date();
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetDashboardDataRequest(processRefId, startDate, new Date()));
		
		if(processRefId!=null){
			String periodicity = "Day";
			action.addRequest(new GetDashboardProcessTrendsRequest(processRefId, startDate, endDate, periodicity));
		}
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				int i = 0;
				GetDashboardDataResponse baseDashboardResp = (GetDashboardDataResponse)aResponse.get(i++);
				getView().setDashboardData(processRefId,baseDashboardResp.getWorkloads(),baseDashboardResp.getProcessesSummaries());
				
				if(processRefId!=null){
					getView().setProcessAgingData(baseDashboardResp.getAging(),baseDashboardResp.getLongTasks());
					
					GetDashboardProcessTrendsResponse processResponse = (GetDashboardProcessTrendsResponse)aResponse.get(i++);
					getView().setProcessTrendsData(processResponse.getStartTrend(), processResponse.getCompletionTrend());
				}
			}
		});
		
	}
}
