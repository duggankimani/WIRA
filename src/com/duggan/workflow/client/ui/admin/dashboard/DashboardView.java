package com.duggan.workflow.client.ui.admin.dashboard;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.FontOptions;
import com.googlecode.gflot.client.options.GridOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.LegendOptions.LegendPosition;
import com.googlecode.gflot.client.options.PlotOptions;
import com.gwtplatform.mvp.client.ViewImpl;

public class DashboardView extends ViewImpl implements
		DashboardPresenter.MyView {
	
	@UiField(provided=true)
	SimplePlot plot;
		
	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, DashboardView> {
	}

	@Inject
	public DashboardView(final Binder binder) {
		renderPlot();
		
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	//@UiFactory
	public SimplePlot renderPlot(){
		PlotModel model = new PlotModel();
        
		PlotOptions plotOptions = PlotOptions.create();
        plotOptions.setLegendOptions( LegendOptions.create().setBackgroundOpacity( 1 )
            .setPosition( LegendPosition.SOUTH_EAST ) );
        plotOptions.setGridOptions( GridOptions.create().setMargin(5) );
        plotOptions.addXAxisOptions( AxisOptions.create().setFont(FontOptions.create().setColor("black").setWeight( "bold" ).setStyle( "italic" ) ) );
        plotOptions.addYAxisOptions( AxisOptions.create().setFont(FontOptions.create().setColor( "black" ).setWeight( "bold" ).setStyle( "italic" ) ) );
        
        // create the plot
        plot = new SimplePlot(model, plotOptions);
        
        generateRandomData();
        return plot;
	}
	
	
	 /**
     * Generate random data
     */
    private void generateRandomData()
    {
        int nbSeries = Random.nextInt(2);
        for ( int i = 0; i < nbSeries; i++ )
        {
            plot.getModel().addSeries( Series.of( "Random Series" + i ) );
        }
        for ( int i = 1; i < 13; i++ )
        {
            for ( SeriesHandler series : plot.getModel().getHandlers() )
            {
                series.add( DataPoint.of( i, Random.nextInt( 30 ) ) );
            }
        }
    }
}
