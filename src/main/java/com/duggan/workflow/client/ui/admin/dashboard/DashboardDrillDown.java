package com.duggan.workflow.client.ui.admin.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.duggan.workflow.client.ui.admin.dashboard.Dashboard.DataPropertyAccess;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.util.NumberUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.GenericFilter;
import com.duggan.workflow.shared.model.dashboard.Data;
import com.duggan.workflow.shared.model.dashboard.EmployeeWorkload;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.model.dashboard.ProcessTrend;
import com.duggan.workflow.shared.model.dashboard.ProcessesSummary;
import com.duggan.workflow.shared.model.dashboard.TaskAging;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FormPanel;

public class DashboardDrillDown extends Composite {

	private static DashboardDrillDownUiBinder uiBinder = GWT
			.create(DashboardDrillDownUiBinder.class);

	@UiField
	Element spnTotal;
	@UiField
	Element spnCompleted;
	@UiField
	Element spnCompletedPerc;
	@UiField
	Element spnInProgress;
	@UiField
	Element spnInProgressPerc;
	@UiField
	Element spnOverdue;
	@UiField
	Element spnOverduePerc;

	@UiField
	Element spnAvgTot;

	@UiField
	Element spnTarget;

	@UiField
	Element employeesTable;

	@UiField
	Element tbodyLongestTasks;

	@UiField
	Element tbodyAging;

	@UiField
	AnchorElement aProcessName;

	@UiField
	Element spnGrowth;
	
	@UiField HTMLPanel panelChart;

	private String processRefId;

	private static final DataPropertyAccess dataAccess = GWT
			.create(DataPropertyAccess.class);

	interface DashboardDrillDownUiBinder extends
			UiBinder<Widget, DashboardDrillDown> {
	}

	public DashboardDrillDown() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setProcessesSummary(ArrayList<ProcessesSummary> list) {

		int sumTotal = 0;
		int sumCompleted = 0;
		int sumInProgress = 0;
		int sumOverdue = 0;
		Double sumCompletedPerc = 0.0;
		Double sumInProgressPerc = 0.0;
		Double sumOverduePerc = 0.0;
		Double sumAvgTot = 0.0;

		StringBuffer html = new StringBuffer();
		for (ProcessesSummary processesSummary : list) {
			Double averageTot = processesSummary.getAvgtot();
			int completed = processesSummary.getCompleted();
			int inProgress = processesSummary.getInprogress();
			String name = processesSummary.getName();
			int overdue = processesSummary.getOverdue();
			String processId = processesSummary.getProcessId();
			processRefId = processesSummary.getRefId();
			int target = processesSummary.getTargetDays();
			int total = inProgress + completed;
			Double completedPerc = (new Double(completed) / total) * 100;
			Double inProgressPerc = (new Double(inProgress) / total) * 100;
			Double overduePerc = (new Double(overdue) / total) * 100;

			sumTotal = sumTotal + total;
			sumCompleted = sumCompleted + completed;
			sumInProgress = sumInProgress + inProgress;
			sumOverdue = sumOverdue + overdue;
			sumCompletedPerc = sumCompletedPerc + completedPerc;
			sumInProgressPerc = sumInProgressPerc + inProgressPerc;
			sumOverduePerc = sumOverduePerc + overduePerc;

			Double growth = ((target - averageTot) / target) * 100;

			aProcessName.setInnerHTML(processesSummary.getName());
			spnAvgTot.setInnerText(averageTot.intValue() + "");
			spnTarget.setInnerText(processesSummary.getTargetDays() + "");

			Element arrowUp = (Element) spnAvgTot.getParentElement()
					.getChild(3);
			if (growth < 0) {
				spnAvgTot.getParentElement().removeClassName("text-success");
				spnAvgTot.getParentElement().addClassName("text-danger");
				arrowUp.removeClassName("icon-arrow-up");
				arrowUp.addClassName("icon-arrow-down");
				spnGrowth.setInnerHTML(growth.intValue() + "% worse");
			} else {
				spnAvgTot.getParentElement().addClassName("text-success");
				spnAvgTot.getParentElement().removeClassName("text-danger");
				arrowUp.addClassName("icon-arrow-up");
				arrowUp.removeClassName("icon-arrow-down");
				spnGrowth.setInnerHTML(growth.intValue() + " better");
			}
			html.append("<tr><td><a href=\"#/dashboards/"
					+ processesSummary.getRefId() + "\">" + name + "</a>"
					+ "</td><td>" + total + "</td><td>" + inProgress + " ("
					+ inProgressPerc.intValue() + "%)</td>" + "<td>" + overdue
					+ "<span class=\"\">(" + overduePerc.intValue()
					+ "%)</span></td><td>" + "<span class=\"\">"
					+ averageTot.intValue() + " days</span></td></tr>");
		}

		if (!list.isEmpty()) {
			sumCompletedPerc = sumCompletedPerc / list.size();
			sumInProgressPerc = sumInProgressPerc / list.size();
			sumOverduePerc = sumOverduePerc / list.size();
		}

		spnTotal.setInnerText(sumTotal + "");
		spnCompleted.setInnerText(sumCompleted + "");
		spnCompletedPerc.setInnerText(sumCompletedPerc.intValue() + "%");
		spnInProgress.setInnerText(sumInProgress + "");
		spnInProgressPerc.setInnerText(sumInProgressPerc.intValue() + "%");
		spnOverdue.setInnerText(sumOverdue + "");
		spnOverduePerc.setInnerText(sumOverduePerc.intValue() + "%");
	}

	public void setWorkflows(ArrayList<EmployeeWorkload> workloads) {

		StringBuffer html = new StringBuffer();
		for (EmployeeWorkload workload : workloads) {
			String ownerid = workload.getOwnerid();
			String fullName = workload.getFullName();
			int inprogress = workload.getInprogress();
			int completed = workload.getCompleted();
			int total = workload.getTotal();
			int overdue = workload.getOverdue();
			Double avg = workload.getAvg();
			Double completedPerc = (new Double(completed) / total) * 100;
			Double inProgressPerc = (new Double(inprogress) / total) * 100;
			Double overduePerc = (new Double(overdue) / total) * 100;

			html.append("<tr>" + "<td>" + "<a href=\"#\">" + fullName + "</a>"
					+ "</td>" + "<td>" + total + "</td>" + "<td>" + inprogress
					+ " (" + inProgressPerc.intValue() + "%)</td>" + "<td>"
					+ overdue + "<span class=\"\">(" + overduePerc.intValue()
					+ "%)</span>" + "</td>" + "</tr>");
		}

		employeesTable.setInnerHTML(html.toString());
	}

	public void setAgingData(ArrayList<TaskAging> taskAging,
			ArrayList<LongTask> longTasks) {

		StringBuffer html = new StringBuffer();
		for (LongTask task : longTasks) {
			String avg = NumberUtils.format(task.getAverageTime(), 1);
			html.append("<tr>" + "<td>" + "<a href=\"#\">" + task.getTaskName()
					+ "</a>" + "</td>" + "<td>" + task.getNoOfTasks()
					+ " tasks</td>" + "<td title=\"" + task.getPeopleNames()
					+ "\">" + task.getPeopleCount() + "</td>" + "<td>" + avg
					+ " days</td>" + "</tr>");
		}
		tbodyLongestTasks.setInnerHTML(html.toString());

		int totalCount = 0;
		for (TaskAging aging : taskAging) {
			totalCount += aging.getTaskCount();
		}
		html = new StringBuffer();
		for (TaskAging aging : taskAging) {
			Double per = (new Double(aging.getTaskCount()) / totalCount) * 100;
			String percentage = NumberUtils.format(per, 1);
			html.append("<tr>" + "<td>" + "	<a>" + aging.getPeriod() + "</a>"
					+ "</td>" + "<td>" + aging.getTaskCount() + "</td>"
					+ "<td>" + "<span>(" + percentage + "%)</span>" + "</td>"
					+ "</tr>");
		}
		tbodyAging.setInnerHTML(html.toString());
	}

	public void setTrendsData(ArrayList<ProcessTrend> startTrend,
			ArrayList<ProcessTrend> completionTrend) {
		// Line Graph
		
		int maxCount = 150;
		HashMap<Integer, Data> data = new HashMap<Integer, Data>();
		for(ProcessTrend trend :startTrend){
			Data d= new Data(trend.getPeriod()+"", new Double(trend.getRequestCount()));
			d.setData2(0.0);
			data.put(trend.getPeriod(), d);
			if(trend.getRequestCount()>maxCount){
				maxCount = trend.getRequestCount();
			}
			//Window.alert("Data {name:"+d.getName()+", data1:"+d.getData1()+", data2:"+d.getData2()+"}");
		}
		
		for(ProcessTrend trend :completionTrend){
			Data item = data.get(trend.getPeriod());
			if(item==null){
				item = new Data(trend.getPeriod()+"", 0.0);
				data.put(trend.getPeriod(), item);
			}
			item.setData2(new Double(trend.getRequestCount()));
			if(trend.getRequestCount()>maxCount){
				maxCount = trend.getRequestCount();
			}
		}
		
		maxCount = 100;
		
		lineStore.clear();
		lineStore.addAll(data.values());
		if(lineChart == null){
			lineChart = new Chart<Data>(600,300);
			createLineGraph(maxCount);
			lineChart.setStore(lineStore);
			
			FormPanel panel = new FormPanel();
			panel.setPixelSize(600, 300);
//			Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
//			resize.setMinHeight(300);
//			resize.setMinWidth(300);
			panel.add(lineChart);
			panelChart.clear();
			panelChart.add(panel);
			
		}else{
			lineChart.redrawChart();
		}
	}

	Chart<Data> lineChart = null;
	
	ListStore<Data> lineStore = new ListStore<Data>(dataAccess.nameKey());

	private GenericFilter filter;

	public Chart<Data> createLineGraph(int maxCount) {
		
		NumericAxis<Data> axis = new NumericAxis<Data>();
		axis.setPosition(Position.LEFT);
		axis.addField(dataAccess.data1());
		axis.addField(dataAccess.data2());
		// axis.addField(dataAccess.data3());
		TextSprite title = new TextSprite("Number of Tasks");
		title.setFontSize(18);
		axis.setTitleConfig(title);
		axis.setMinorTickSteps(1);
		axis.setDisplayGrid(true);
		PathSprite odd = new PathSprite();
		odd.setOpacity(1);
		odd.setFill(new Color("#ddd"));
		odd.setStroke(new Color("#bbb"));
		odd.setStrokeWidth(0.5);
		axis.setGridOddConfig(odd);
		axis.setMinimum(0);
		axis.setMaximum(maxCount);
		

		CategoryAxis<Data, String> catAxis = new CategoryAxis<Data, String>();
		catAxis.setPosition(Position.BOTTOM);
		catAxis.setField(dataAccess.name());
		title = new TextSprite("Month of the Year");
		title.setFontSize(18);
		catAxis.setTitleConfig(title);
		catAxis.setLabelProvider(new LabelProvider<String>() {
			@Override
			public String getLabel(String item) {
				return item.substring(0, 3);
			}
		});
		

		SeriesToolTipConfig<Data> toolTip = new SeriesToolTipConfig<Data>();
		toolTip.setTrackMouse(true);
		toolTip.setHideDelay(200);
		toolTip.setLabelProvider(new SeriesLabelProvider<Data>() {

			@Override
			public String getLabel(Data item,
					ValueProvider<? super Data, ? extends Number> valueProvider) {
				return "(" + valueProvider.getValue(item) + ")";
			}
		});

		final LineSeries<Data> series = new LineSeries<Data>();
		series.setYAxisPosition(Position.LEFT);
		series.setYField(dataAccess.data1());
		series.setStroke(new RGB(194, 0, 36));
		series.setShowMarkers(true);
		Sprite marker = Primitives.square(0, 0, 6);
		marker.setFill(new RGB(254, 100, 36));
		series.setMarkerConfig(marker);
		series.setHighlighting(true);
		series.setToolTipConfig(toolTip);
		series.setLegendTitle("Requested");
		

		final LineSeries<Data> series2 = new LineSeries<Data>();
		series2.setYAxisPosition(Position.LEFT);
		series2.setYField(dataAccess.data2());
		series2.setStroke(new RGB(240, 165, 10));
		series2.setShowMarkers(true);
		series2.setSmooth(true);
		marker = Primitives.circle(0, 0, 6);
		marker.setFill(new RGB(240, 165, 10));
		series2.setMarkerConfig(marker);
		series2.setHighlighting(true);
		series2.setToolTipConfig(toolTip);
		series2.setLegendTitle("Completed");
		

//		 final LineSeries<Data> series3 = new LineSeries<Data>();
//		 series3.setYAxisPosition(Position.LEFT);
//		 series3.setYField(dataAccess.data3());
//		 series3.setStroke(new RGB(32, 68, 186));
//		 series3.setShowMarkers(true);
//		 series3.setSmooth(true);
//		 series3.setFill(new RGB(32, 68, 186));
//		 marker = Primitives.diamond(0, 0, 6);
//		 marker.setFill(new RGB(32, 68, 186));
//		 series3.setMarkerConfig(marker);
//		 series3.setHighlighting(true);
//		 series3.setToolTipConfig(toolTip);
//		 lineChart.addSeries(series3);

		final Legend<Data> legend = new Legend<Data>();
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);
		legend.getBorderConfig().setStrokeWidth(0);
		
		
		lineChart.addAxis(axis);
		lineChart.addAxis(catAxis);
		lineChart.setShadowChart(false);
		lineChart.addSeries(series);
		lineChart.addSeries(series2);
		lineChart.setLegend(legend);
		lineChart.setAnimated(true);
		lineChart.setShadow(false);

		// final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
		// resize.setMinHeight(400);
		// resize.setMinWidth(400);

		return lineChart;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		initDatePicker(getElement());
	}

	public native void initDatePicker(Element parent)/*-{
		var instance = this;
		
		var start = instance.@com.duggan.workflow.client.ui.admin.dashboard.DashboardDrillDown::getStart()();
		if(start==null){
			start = $wnd.moment().subtract(29, 'days');
		}else{
			start = $wnd.moment(new Date(start));
		}
		
		var end = instance.@com.duggan.workflow.client.ui.admin.dashboard.DashboardDrillDown::getEnd()();
		if(end == null){
			end = $wnd.moment();
		}else{
			end = $wnd.moment(new Date(end));
		}
	
		function valueChanged(start, end, fireSearchEvent){
			$wnd.jQuery(parent).find('#range-drilldown span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
	        
	        start = new $wnd.Date(start);
	        end = new $wnd.Date(end);
	        instance.@com.duggan.workflow.client.ui.admin.dashboard.DashboardDrillDown::onDateChange(Lcom/google/gwt/core/client/JsDate;Lcom/google/gwt/core/client/JsDate;Ljava/lang/Boolean;)(start, end, @java.lang.Boolean::valueOf(Z)(fireSearchEvent));
		}
		
	    function cb(start, end) {
	        valueChanged(start, end, true);
	    }
	
	    var daterange = $wnd.jQuery(parent).find('#range-drilldown').daterangepicker({
	    	showDropdowns: true,
	    	alwaysShowCalendars: true,
	    	format: 'YYYY-MM-DD',
	        startDate: start,
	        endDate: end,
	        parentEl: 'range-drilldown',
	        ranges: {
	           'Today': [$wnd.moment(), $wnd.moment()],
	           'Yesterday': [$wnd.moment().subtract(1, 'days'), $wnd.moment().subtract(1, 'days')],
	           'Last 7 Days': [$wnd.moment().subtract(6, 'days'), $wnd.moment()],
	           'Last 30 Days': [$wnd.moment().subtract(29, 'days'), $wnd.moment()],
	           'This Month': [$wnd.moment().startOf('month'), $wnd.moment().endOf('month')],
	           'Last Month': [$wnd.moment().subtract(1, 'month').startOf('month'), $wnd.moment().subtract(1, 'month').endOf('month')]
	        }
	    }, cb);
	
	    valueChanged(start, end, false);
	}-*/;

	public void onDateChange(JsDate jsStartDate, JsDate jsEndDate, Boolean fireSearchEvent){
		
		if(jsStartDate!=null && jsEndDate!=null){
			Date startDate = new Date(new Double(jsStartDate.getTime()).longValue());
			Date endDate = new Date(new Double(jsEndDate.getTime()).longValue());
			filter = new GenericFilter();
			filter.setStartDate(startDate);
			filter.setEndDate(endDate);
			if(fireSearchEvent){
				AppContext.fireEvent(new SearchEvent(filter));
			}
			
		}
	}

	public String getStart(){
		if(filter==null){
			return null;
		}
		return filter.getStartDate().getTime()+"";
	}
	
	public String getEnd(){
		if(filter==null){
			return null;
		}
		return filter.getEndDate().getTime()+"";
	}
	
	public void setDates(Date startDate, Date endDate) {
		if(startDate==null || endDate==null){
			return;
		}
		if(filter!=null){
			filter.setStartDate(startDate);
			filter.setEndDate(endDate);
		}
		
		setJsDates(getElement(), JsDate.create(startDate.getTime()), JsDate.create(endDate.getTime()));
	}
	
	private native void setJsDates(Element parent, JsDate start, JsDate end)/*-{
		start = $wnd.moment(start);
		end = $wnd.moment(end);
		$wnd.jQuery(parent).find('#range-drilldown span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
	}-*/;
}
