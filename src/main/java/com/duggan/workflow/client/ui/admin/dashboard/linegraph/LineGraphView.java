package com.duggan.workflow.client.ui.admin.dashboard.linegraph;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.Data;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.PieSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class LineGraphView extends ViewImpl implements
		LineGraphPresenter.ILineGraphView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, LineGraphView> {
	}

	public interface DataPropertyAccess extends PropertyAccess<Data> {
	    ValueProvider<Data, Number> data1();
	 
	    ValueProvider<Data, Number> data2();
	 
	   // ValueProvider<Data, Number> data3();
	 
	    ValueProvider<Data, String> name();
	 
	    @Path("name")
	    ModelKeyProvider<Data> nameKey();
	  }
	 
	@UiField
	Chart<Data> chart;

	private static final DataPropertyAccess dataAccess = GWT
			.create(DataPropertyAccess.class);;

	ListStore<Data> store = null;

	@Inject
	public LineGraphView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	NumericAxis<Data> axis = new NumericAxis<Data>();
	public Widget asWidget() {
	    if (store == null) {
	      store = new ListStore<Data>(dataAccess.nameKey());
	      chart.setStore(store);
	      chart.setShadowChart(false);
	 	      
	      axis.setPosition(Position.LEFT);
	      axis.addField(dataAccess.data1());
	      axis.addField(dataAccess.data2());
	    //  axis.addField(dataAccess.data3());
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
	      axis.setMaximum(100);
	      chart.addAxis(axis);
	 
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
	      chart.addAxis(catAxis);
	 
	      SeriesToolTipConfig<Data> toolTip = new SeriesToolTipConfig<Data>();
		    toolTip.setTrackMouse(true);
		    toolTip.setHideDelay(200);
		    toolTip.setLabelProvider(new SeriesLabelProvider<Data>() {
		 
		      @Override
		      public String getLabel(Data item, ValueProvider<? super Data, ? extends Number> valueProvider) {
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
	      chart.addSeries(series);
	 
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
	      chart.addSeries(series2);
	 
//	      final LineSeries<Data> series3 = new LineSeries<Data>();
//	      series3.setYAxisPosition(Position.LEFT);
//	      series3.setYField(dataAccess.data3());
//	      series3.setStroke(new RGB(32, 68, 186));
//	      series3.setShowMarkers(true);
//	      series3.setSmooth(true);
//	      series3.setFill(new RGB(32, 68, 186));
//	      marker = Primitives.diamond(0, 0, 6);
//	      marker.setFill(new RGB(32, 68, 186));
//	      series3.setMarkerConfig(marker);
//	      series3.setHighlighting(true);
//	      series3.setToolTipConfig(toolTip);
//	      chart.addSeries(series3);
	 
	      final Legend<Data> legend = new Legend<Data>();
	      legend.setItemHighlighting(true);
	      legend.setItemHiding(true);
	      legend.getBorderConfig().setStrokeWidth(0);	      
	      chart.setLegend(legend);
	      
	      chart.setAnimated(true);
	      chart.setShadow(false);
	 
//	      final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
//	      resize.setMinHeight(400);
//	      resize.setMinWidth(400);
	 
	      chart.setLayoutData(new VerticalLayoutData(1, 1));
	    }
	    	 
	    return widget;
	  }

	private void setColors(PieSeries<Data> series) {
		Gradient slice1 = new Gradient("slice1", 45);
		// slice1.setColor("#E5412D;");
		slice1.addStop(new Stop(0, new RGB(82, 144, 233)));
		slice1.addStop(new Stop(100, new RGB(82, 144, 233)));
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
		slice4.addStop(new Stop(0, new RGB("#FF4500")));
		slice4.addStop(new Stop(100, new RGB("#FF4500")));
		chart.addGradient(slice4);

		Gradient slice5 = new Gradient("slice5", 45);
		slice5.addStop(new Stop(0, new RGB("#3BDA00")));
		slice5.addStop(new Stop(100, new RGB("#3BDA00")));
		chart.addGradient(slice5);

		Gradient slice6 = new Gradient("slice6", 45);
		slice6.addStop(new Stop(0, new RGB("#34D800")));
		slice6.addStop(new Stop(100, new RGB("#34D800")));
		chart.addGradient(slice6);

		series.addColor(slice1);
		series.addColor(slice2);
		series.addColor(slice3);
		series.addColor(slice4);
		series.addColor(slice5);
		series.addColor(slice6);
	}

	public void setData(ArrayList<Data> data) {
		double max = axis.getMaximum();
		for(Data d: data){
			double big = Math.max(d.getData1(), d.getData2());
			max = Math.max(max, big);
		}
		axis.setMaximum(max);
		
		store.addAll(data);
		chart.redrawChart();
	}

}
