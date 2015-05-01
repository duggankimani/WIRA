package com.duggan.workflow.client.ui.grid;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class OnAggregationFieldChangedHandler implements ValueChangeHandler<Number>{
	private Number previousValue = null;
	private AggregationGrid grid;
	private ColumnConfig config;
	
	public OnAggregationFieldChangedHandler(AggregationGrid grid, ColumnConfig column, Number initial){
		if(initial==null){
			initial=0.0;
		}
		
		this.previousValue= initial;
		this.grid = grid;
		this.config = column;
		grid.aggregate(config,0, initial);
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<Number> event) {
		Number newValue=event.getValue();
		if(previousValue==null){
			previousValue=0;
		}
		
		if(newValue==null){
			newValue=0;
		}
		
		grid.aggregate(config, previousValue, newValue);
		
		this.previousValue=newValue;
		grid.createFooter();
	}
}