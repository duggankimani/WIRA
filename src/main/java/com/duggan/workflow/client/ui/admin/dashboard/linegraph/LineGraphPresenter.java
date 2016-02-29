package com.duggan.workflow.client.ui.admin.dashboard.linegraph;

import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.dashboard.ChartType;
import com.duggan.workflow.shared.model.dashboard.Data;
import com.duggan.workflow.shared.requests.GetTaskCompletionRequest;
import com.duggan.workflow.shared.responses.GetTaskCompletionResponse;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class LineGraphPresenter extends
		PresenterWidget<LineGraphPresenter.ILineGraphView> {

	public interface ILineGraphView extends View {
		void setData(List<Data> data);
	}
	
	@Inject DispatchAsync requestHelper;
	ChartType type;
	boolean loaded=false;
	
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
		if(loaded){
			return;
		}
		loaded=true;
		requestHelper.execute(new GetTaskCompletionRequest(), new TaskServiceCallback<GetTaskCompletionResponse>() {
			@Override
			public void processResult(GetTaskCompletionResponse aResponse) {
				
				getView().setData(aResponse.getData());
			}
		});
	}

	public void setChart(ChartType type) {
		this.type =type; 		
	}
}
