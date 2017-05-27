package com.duggan.workflow.client.ui.admin.dashboard;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.Data;
import com.duggan.workflow.shared.model.dashboard.EmployeeWorkload;
import com.duggan.workflow.shared.model.dashboard.ProcessesSummary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.GaugeAxis;
import com.sencha.gxt.chart.client.chart.series.GaugeSeries;
import com.sencha.gxt.chart.client.chart.series.PieSeries;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Region;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.fx.client.easing.ElasticIn;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FormPanel;

public class Dashboard extends Composite {

	private static DashboardUiBinder uiBinder = GWT
			.create(DashboardUiBinder.class);

	interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {
	}

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
	Element processesTable;
	@UiField
	Element employeesTable;

	@UiField
	HTMLPanel pieChart;
	@UiField
	HTMLPanel gaugeChart;

	private static final DataPropertyAccess dataAccess = GWT
			.create(DataPropertyAccess.class);
	ListStore<Data> pieStore = null;
	ListStore<Data> gaugeStore = null;

	public Dashboard() {
		initWidget(uiBinder.createAndBindUi(this));
		pieStore = new ListStore<Data>(dataAccess.nameKey());
		gaugeStore = new ListStore<Data>(dataAccess.nameKey());
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
			String processRefId = processesSummary.getRefId();
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

			html.append("<tr><td><a href=\"#/dashboards/"+processesSummary.getRefId()+"\">" 
					+ name + "</a>" + "</td><td>"
					+ total + "</td><td>" + inProgress + " ("
					+ inProgressPerc.intValue() + "%)</td>" + "<td>" + overdue
					+ "<span class=\"\">(" + overduePerc.intValue()
					+ "%)</span></td><td>" + "<span class=\"\">"
					+ averageTot.intValue() + " days</span></td></tr>");
		}
		processesTable.setInnerHTML(html.toString());

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

		pieStore.clear();
		for (ProcessesSummary summ : list) {
			Data data = new Data();
			data.setName(summ.getName());
			data.setData1(summ.getTotal());
			data.setData2(summ.getOverdue());
			pieStore.add(data);
		}
		
		//Pie Chart
		Chart<Data> chart = createPieChart(dataAccess.data1());
		FormPanel panel = new FormPanel();
		panel.setPixelSize(350, 350);
		Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
		resize.setMinHeight(300);
		resize.setMinWidth(300);
		panel.add(chart);
		pieChart.clear();
		pieChart.add(panel);
		
		//Gauge Chart
		gaugeStore.clear();
		gaugeStore.add(new Data("Overdue", sumOverduePerc.intValue(), "Overdue Items"));
		gaugeStore.add(new Data("Total", sumTotal, "Total"));
		Chart<Data> gauge = createGauge(dataAccess.data1());
		panel = new FormPanel();
		panel.setLayoutData(new MarginData(40,0,0,0));
		panel.setPixelSize(350, 200);
		resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
		resize.setMinHeight(150);
		resize.setMinWidth(200);
		panel.add(gauge);
		gauge.setDefaultInsets(new Region(40, 60, 10, 60));
		gaugeChart.clear();
		gaugeChart.add(panel);

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

	public Chart<Data> createGauge(ValueProvider<Data, Double> provider) {
		Chart<Data> chart = new Chart<Data>();
		chart.setStore(gaugeStore);
		chart.setAnimationDuration(750);
		chart.setAnimationEasing(new ElasticIn());

		GaugeAxis<Data> axis = new GaugeAxis<Data>();
		axis.setDisplayGrid(true);
		axis.setMinimum(0);
		axis.setMaximum(100);
		
		final GaugeSeries<Data> gauge = new GaugeSeries<Data>();
		gauge.addColor(new RGB("#b50a12"));
		gauge.addColor(new RGB("#3d920f"));
		gauge.setAngleField(provider);
		gauge.setNeedle(false);
		gauge.setDonut(20);
		chart.addAxis(axis);
		chart.addSeries(gauge);

		chart.setLayoutData(new VerticalLayoutData(1, 1));
		return chart;
	}

	private Chart<Data> createPieChart(ValueProvider<Data, Double> data1) {
		final Chart<Data> chart = new Chart<Data>();
		chart.setDefaultInsets(50);
		chart.setStore(pieStore);
		chart.setShadowChart(false);

		Gradient slice1 = new Gradient(45);
		slice1.addStop(new Stop(0, new RGB(148, 174, 10)));
		slice1.addStop(new Stop(100, new RGB(107, 126, 7)));
		chart.addGradient(slice1);

		Gradient slice2 = new Gradient(45);
		slice2.addStop(new Stop(0, new RGB(17, 95, 166)));
		slice2.addStop(new Stop(100, new RGB(12, 69, 120)));
		chart.addGradient(slice2);

		Gradient slice3 = new Gradient(45);
		slice3.addStop(new Stop(0, new RGB(166, 17, 32)));
		slice3.addStop(new Stop(100, new RGB(120, 12, 23)));
		chart.addGradient(slice3);

		Gradient slice4 = new Gradient(45);
		slice4.addStop(new Stop(0, new RGB(255, 136, 9)));
		slice4.addStop(new Stop(100, new RGB(213, 110, 0)));
		chart.addGradient(slice4);

		final PieSeries<Data> series = new PieSeries<Data>();
		series.setAngleField(dataAccess.data1());
		series.addColor(slice1);
		series.addColor(slice2);
		series.addColor(slice3);
		series.addColor(slice4);
		TextSprite textConfig = new TextSprite();
		textConfig.setFont("Arial");
		textConfig.setTextBaseline(TextBaseline.MIDDLE);
		textConfig.setFontSize(12);
		textConfig.setTextAnchor(TextAnchor.MIDDLE);
		textConfig.setZIndex(15);
		
		SeriesLabelConfig<Data> labelConfig = new SeriesLabelConfig<Data>();
		labelConfig.setSpriteConfig(textConfig);
		labelConfig.setLabelPosition(LabelPosition.START);
		labelConfig.setValueProvider(dataAccess.name(),
				new StringLabelProvider<String>(){
			@Override
			public String getLabel(String item) {
				return item.substring(0, 4);
			}
		});
		series.setDonut(50);
		series.setLabelConfig(labelConfig);
		series.setHighlighting(true);
		series.setLegendValueProvider(dataAccess.name(),
				new LabelProvider<String>() {

					@Override
					public String getLabel(String item) {
						//return item.substring(0, 3);
						return item.substring(0, 8);
					}
				});
		chart.addSeries(series);

		final Legend<Data> legend = new Legend<Data>();
		legend.setPosition(Position.BOTTOM);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);
		legend.getBorderConfig().setStrokeWidth(0);
		chart.setLegend(legend);

		return chart;
	}

	public interface DataPropertyAccess extends PropertyAccess<Data> {
		ValueProvider<Data, Double> data1();

		ValueProvider<Data, Double> data2();

		ValueProvider<Data, Double> data3();

		ValueProvider<Data, Double> data4();

		ValueProvider<Data, String> name();

		@Path("name")
		ModelKeyProvider<Data> nameKey();
	}
}
