package com.duggan.workflow.client.ui.admin.dashboard;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.dashboard.charts.PieChartPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.linegraph.LineGraphPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.table.TableDataPresenter;
import com.duggan.workflow.shared.model.dashboard.ChartType;
import com.duggan.workflow.shared.requests.GetDashBoardDataRequest;
import com.duggan.workflow.shared.responses.GetDashBoardDataResponse;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;

public class DashboardPresenter extends
		PresenterWidget<DashboardPresenter.IDashboardView> {

	public interface IDashboardView extends View {

		void setValues(Integer requestCount, Integer activeCount,
				Integer failureCount);
		
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
	
	@Inject DispatchAsync requestHelper;
	
	@Inject
	public DashboardPresenter(final EventBus eventBus, final IDashboardView view,
			Provider<PieChartPresenter>pieChartProvider,
			Provider<LineGraphPresenter>lineGraphProvider,
			Provider<TableDataPresenter>tableDataProvider) {
		super(eventBus, view);
		pieChartFactory = new StandardProvider<PieChartPresenter>(pieChartProvider);
		lineGraphFactory = new StandardProvider<LineGraphPresenter>(lineGraphProvider);
		tableFactory = new StandardProvider<TableDataPresenter>(tableDataProvider);
	}

	@Override
	protected void onReset() {
		super.onReset();
		loadCharts();
	}
	
	boolean loaded=false;
	int i;
	private void loadCharts() {
		if(loaded){
			//System.err.println("Reset Called Again..........."+(++i));
			return;
		}
		
		loaded=true;
		
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
