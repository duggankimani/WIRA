package com.duggan.workflow.client.ui.admin.dashboard;

import com.duggan.workflow.client.util.tests.Data;
import com.duggan.workflow.client.util.tests.TestData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.series.PieSeries;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;

public class DashboardView extends ViewImpl implements
		DashboardPresenter.MyView {

	public interface Binder extends UiBinder<Widget, DashboardView> {
	}

	private Widget widget;

	public interface DataPropertyAccess extends PropertyAccess<Data> {

		ValueProvider<Data, Double> data1();

		ValueProvider<Data, String> name();

		@Path("name")
		ModelKeyProvider<Data> nameKey();
	}


	@UiField Chart<Data> chart;
	
	PieSeries<Data> series;
	
	private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);;

	@UiField HTMLPanel panelTurnAroundTime;
	
	@Inject
	public DashboardView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	public void createPieChart(){
		//Chart<Data> chart = new Chart<Data>();
		chart.setWidth("100%");
		chart.setHeight("335px");
		final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
		store.addAll(TestData.getInvoiceData(4, 20, 100));

//		chart.setDefaultInsets(50);
		chart.setStore(store);
		//chart.setShadowChart(true);
		chart.setAnimated(true);

		Gradient slice1 = new Gradient("slice1", 45);
		slice1.setColor("#E5412D;");
//		slice1.addStop(new Stop(0, new RGB(82, 144, 233)));
//		slice1.addStop(new Stop(100, new RGB(82, 144, 233)));
		chart.addGradient(slice1);

		Gradient slice2 = new Gradient("slice2", 45);
		slice2.addStop(new Stop(0, new RGB(113, 179, 124)));
		slice2.addStop(new Stop(100, new RGB(113, 179, 124)));
		chart.addGradient(slice2);

		Gradient slice3 = new Gradient("slice3", 45);
		slice3.addStop(new Stop(0, new RGB(236, 247, 47)));
		slice3.addStop(new Stop(100, new RGB(236, 247, 47)));
		chart.addGradient(slice3);

		Gradient slice4 = new Gradient("slice4", 45);
		slice4.addStop(new Stop(0, new RGB(234, 144, 150)));
		slice4.addStop(new Stop(100, new RGB(234, 144, 150)));
		chart.addGradient(slice4);

		series = new PieSeries<Data>();
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
				new StringLabelProvider<String>());
		series.setLabelConfig(labelConfig);
		series.setHighlighting(true);
		series.setLegendValueProvider(dataAccess.name(),
				new LabelProvider<String>() {

					@Override
					public String getLabel(String item) {
						//return item.substring(0, 3);
						return item;
					}
				});
		chart.addSeries(series);
		series.setDonut(54);
		

		SeriesToolTipConfig<Data> toolTip = new SeriesToolTipConfig<Data>();
	    toolTip.setTrackMouse(true);
	    toolTip.setHideDelay(200);
	    toolTip.setLabelProvider(new SeriesLabelProvider<Data>() {
	 
	      @Override
	      public String getLabel(Data item, ValueProvider<? super Data, ? extends Number> valueProvider) {
	        return "(" + valueProvider.getValue(item) + ")";
	      }
	    });
	    series.setToolTipConfig(toolTip);
	    
		final Legend<Data> legend = new Legend<Data>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);
		chart.setLegend(legend);
		
		//panelTurnAroundTime.add(chart);
		final Resizable resize = new Resizable(chart, Dir.E, Dir.SE, Dir.S);
		resize.setMinHeight(400);
		resize.setMinWidth(400);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
