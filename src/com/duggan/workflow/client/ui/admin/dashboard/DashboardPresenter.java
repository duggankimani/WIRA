package com.duggan.workflow.client.ui.admin.dashboard;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.admin.dashboard.charts.PieChartPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.linegraph.LineGraphPresenter;
import com.duggan.workflow.shared.model.dashboards.ChartType;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;

public class DashboardPresenter extends
		PresenterWidget<DashboardPresenter.MyView> {

	public interface MyView extends View {
		
	}

	private IndirectProvider<PieChartPresenter> pieChartFactory;
	private IndirectProvider<LineGraphPresenter> lineGraphFactory;
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> OVERALLTURNAROUND_SLOT = new Type<RevealContentHandler<?>>();
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> REQUESTSPERDOC_SLOT = new Type<RevealContentHandler<?>>();
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> LINEGRAPH_SLOT = new Type<RevealContentHandler<?>>();
	
	
	@Inject
	public DashboardPresenter(final EventBus eventBus, final MyView view,
			Provider<PieChartPresenter>pieChartProvider,
			Provider<LineGraphPresenter>lineGraphProvider) {
		super(eventBus, view);
		pieChartFactory = new StandardProvider<PieChartPresenter>(pieChartProvider);
		lineGraphFactory = new StandardProvider<LineGraphPresenter>(lineGraphProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		loadCharts();
	}

	private void loadCharts() {
		setInSlot(OVERALLTURNAROUND_SLOT, null);
		setInSlot(REQUESTSPERDOC_SLOT, null);
		setInSlot(LINEGRAPH_SLOT, null);
		pieChartFactory.get(new ServiceCallback<PieChartPresenter>() {
			@Override
			public void processResult(PieChartPresenter aResponse) {
				aResponse.setChart(ChartType.AGINGANALYSIS);
				setInSlot(OVERALLTURNAROUND_SLOT, aResponse);
			}
		});
		
		pieChartFactory.get(new ServiceCallback<PieChartPresenter>() {
			@Override
			public void processResult(PieChartPresenter aResponse) {
				aResponse.setChart(ChartType.AVGTURNAROUNDPERDOC);
				setInSlot(REQUESTSPERDOC_SLOT, aResponse);
			}
		});
		
		lineGraphFactory.get(new ServiceCallback<LineGraphPresenter>() {
			@Override
			public void processResult(LineGraphPresenter aResponse) {
				setInSlot(LINEGRAPH_SLOT, aResponse);
			}
		});
	}
}
