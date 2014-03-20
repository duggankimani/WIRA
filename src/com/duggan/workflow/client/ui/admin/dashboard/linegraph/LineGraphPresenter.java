package com.duggan.workflow.client.ui.admin.dashboard.linegraph;

import java.util.List;

import com.duggan.workflow.client.util.tests.Data;
import com.duggan.workflow.client.util.tests.TestData;
import com.duggan.workflow.shared.model.dashboards.ChartType;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class LineGraphPresenter extends
		PresenterWidget<LineGraphPresenter.ILineGraphView> {

	public interface ILineGraphView extends View {
		void setData(List<Data> data);
	}
	
	@Inject DispatchAsync requestHelper;
	ChartType type;
	
	@Inject
	public LineGraphPresenter(final EventBus eventBus, final ILineGraphView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		loadData();
	}
	
	public void loadData(){
		getView().setData(TestData.getData(8, 20, 100));
//		if(type!=null){
//			getView().setData(TestData.getData(type));
//		}
	}

	public void setChart(ChartType type) {
		this.type =type; 		
	}
}
