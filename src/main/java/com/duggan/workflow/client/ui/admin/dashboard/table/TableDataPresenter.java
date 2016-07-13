package com.duggan.workflow.client.ui.admin.dashboard.table;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.ChartType;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.requests.GetLongTasksRequest;
import com.duggan.workflow.shared.responses.GetLongTasksResponse;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.duggan.workflow.client.service.TaskServiceCallback;

public class TableDataPresenter extends
		PresenterWidget<TableDataPresenter.ITableDataView> {

	public interface ITableDataView extends View {

		void setTasks(ArrayList<LongTask> longTasks);
	}
	
	@Inject DispatchAsync requestHelper;
	ChartType type;
	
	boolean loaded = false;
	
	@Inject
	public TableDataPresenter(final EventBus eventBus, final ITableDataView view) {
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
		requestHelper.execute(new GetLongTasksRequest(), new TaskServiceCallback<GetLongTasksResponse>() {
			@Override
			public void processResult(GetLongTasksResponse aResponse) {
				getView().setTasks(aResponse.getLongTasks());
			}
		});
	}

}
