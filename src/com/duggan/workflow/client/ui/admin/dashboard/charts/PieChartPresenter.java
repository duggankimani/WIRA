package com.duggan.workflow.client.ui.admin.dashboard.charts;

import java.util.List;

import com.duggan.workflow.shared.model.dashboard.ChartType;
import com.duggan.workflow.shared.model.dashboard.Data;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PieChartPresenter extends
		PresenterWidget<PieChartPresenter.IPieChartView> {

	public interface IPieChartView extends View {

		void setData(List<Data> data);
	}
	
	@Inject DispatchAsync requestHelper;
	ChartType type;
	
	@Inject
	public PieChartPresenter(final EventBus eventBus, final IPieChartView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void setChart(ChartType type) {
		this.type =type; 		
	}

	public void setValues(List<Data> data) {
		getView().setData(data);
	}
}
