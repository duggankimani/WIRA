package com.duggan.workflow.client.ui.admin.dashboard;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.EmployeeWorkload;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.model.dashboard.ProcessTrend;
import com.duggan.workflow.shared.model.dashboard.ProcessesSummary;
import com.duggan.workflow.shared.model.dashboard.TaskAging;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DashboardView extends ViewImpl implements
		DashboardPresenter.IDashboardView {

	public interface Binder extends UiBinder<Widget, DashboardView> {
	}

	private Widget widget;

	@UiField Element divBase;
	@UiField Element divDrillDown;
	@UiField Dashboard baseDashboard;
	@UiField DashboardDrillDown drilldownDashboard;
	
	@Inject
	public DashboardView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setDashboardData(String processRefId, ArrayList<EmployeeWorkload> workloads,
			ArrayList<ProcessesSummary> processesSummary) {
		if(processRefId==null){
			divBase.removeClassName("hide");
			divDrillDown.addClassName("hide");
			baseDashboard.setWorkflows(workloads);
			baseDashboard.setProcessesSummary(processesSummary);
		}else{
			divBase.addClassName("hide");
			divDrillDown.removeClassName("hide");
			drilldownDashboard.setWorkflows(workloads);
			drilldownDashboard.setProcessesSummary(processesSummary);
		}
		
	}

	@Override
	public void setProcessAgingData(ArrayList<TaskAging> aging,
			ArrayList<LongTask> longTasks) {
		drilldownDashboard.setAgingData(aging, longTasks);
		
	}

	@Override
	public void setProcessTrendsData(ArrayList<ProcessTrend> startTrend,
			ArrayList<ProcessTrend> completionTrend) {
		drilldownDashboard.setTrendsData(startTrend, completionTrend);
	}
	
}
