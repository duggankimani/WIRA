package com.duggan.workflow.client.ui.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.util.NumberUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class AggregationGrid extends Composite {

	private static AggregationGridUiBinder uiBinder = GWT
			.create(AggregationGridUiBinder.class);

	interface AggregationGridUiBinder extends UiBinder<Widget, AggregationGrid> {
	}
	
	@UiField TableView tblView;
	@UiField SpanElement spnAggregate;
	
	HashMap<String, Number> summaries = new HashMap<String, Number>();

	ArrayList<ColumnConfig> columnConfigs = null;
	
	public AggregationGrid() {
		initWidget(uiBinder.createAndBindUi(this));
		setData(new ArrayList<DataModel>());
	}
	
	public AggregationGrid(ArrayList<ColumnConfig> configs){
		this();
		setColumnConfigs(configs);
	}	

	private void createHeaders(ArrayList<ColumnConfig> configs) {
		this.columnConfigs = configs;
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		
		ArrayList<String> headers = new ArrayList<String>();
		if(configs!=null)
		for(ColumnConfig config: configs){
			headers.add("generic-header");
			widgets.add(config.createHeaderWidget());
		}	
		tblView.setHeaderWidgets(headers,widgets);
		createFooter();
	}

	public void setData(ArrayList<DataModel> models){
		tblView.clearRows();
		summaries.clear();
		
		if(models!=null)
		for(DataModel row: models){
			addRowData(row);
		}
		
		//addRowData(new DataModel());
		createFooter();
	}
	
	public void addRowData(DataModel data){
		AggregationGridRow row = new AggregationGridRow(this, data, columnConfigs);	
		tblView.addRow(row);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addValueChangeHandler(ColumnConfig config, Widget widget) {
		Object value = ((HasValue)widget).getValue();
		Number initial= 0.0;
		if(value!=null){
			initial = (Number)value;
		}
		HasValueChangeHandlers hasValueChangeHandlers = (HasValueChangeHandlers)widget;
		hasValueChangeHandlers.addValueChangeHandler(
				new OnAggregationFieldChangedHandler(this,config,initial){
					@Override
					public void onValueChange(ValueChangeEvent<Number> event) {
						super.onValueChange(event);
						//check need change
						//createRowLast();
					}
				});
	}
	
	

	protected void createRowLast() {
				
		int count = tblView.getRowCount();
		if(count==0){
			addRowData(new DataModel());
		}else{
			//addRowData(new DataModel());
			Widget w = tblView.getRow(count-1);
			DataModel lastRowData=null;
			
			assert w!=null;
			
			if(w instanceof AggregationGridRow){
				AggregationGridRow row = (AggregationGridRow)w;
				lastRowData = row.getData();
			}
			//System.err.println(lastRowData);
			if(lastRowData!=null && !lastRowData.isEmpty() && columnConfigs!=null && !columnConfigs.isEmpty()){
				//ColumnConfig config = columnConfigs.get(columnConfigs.size()-1);
				//Any column
				boolean add = true;
				boolean hasValue=false;
				for(ColumnConfig config: columnConfigs){
					if(config.isMandatory() && lastRowData.get(config.getKey())==null){
						add=false;
					}
					
					if(lastRowData.get(config.getKey())!=null){
						hasValue=true;
					}
				}
				
				if(add && hasValue){
					addRowData(new DataModel());
				}
			}
			
		}
	}

	public void aggregate(ColumnConfig column, Number previous, Number newValue) {
		String key = column.getKey();
		Number sum = summaries.get(key);
		if(sum==null){
			sum=0.0;
		}		
		
		sum = sum.doubleValue()-previous.doubleValue()+newValue.doubleValue();
		summaries.put(key, sum);
		createFooter();		
	}

	public void createFooter() {
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		
		if(columnConfigs!=null)
		for(ColumnConfig config: columnConfigs){
			if(config.isAggregationColumn()){
				
				Number value = summaries.get(config.getKey());
				widgets.add(new SummaryRenderer(value));
				
			}else{
				widgets.add(new InlineLabel());
			}
			
		}	
		tblView.setFooter(widgets);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createHeaders(columnConfigs);
	}
	
	public ArrayList<ColumnConfig> getColumnConfigs() {
		return columnConfigs;
	}

	public void setColumnConfigs(ArrayList<ColumnConfig> columnConfigs) {
		this.columnConfigs= columnConfigs;
	}
	
	public void setAutoNumber(boolean enable){
		tblView.setAutoNumber(enable);
	}
	
	public ArrayList<DataModel> getData(){
		ArrayList<DataModel> models = new ArrayList<DataModel>();
		int rows = tblView.getRowCount();
		if(rows>0){
			for(int row=0; row<rows; row++){
				Widget rowWidget = tblView.getRow(row);
				if(rowWidget instanceof AggregationGridRow){
					AggregationGridRow r = (AggregationGridRow)rowWidget;
					models.add(r.getData());	
				}
			}
		}
		
		return models;
	}
	
	public <T> ArrayList<T> getData(DataMapper mapper){
		ArrayList<T> vals = new ArrayList<T>();		
		
		int rows = tblView.getRowCount();
		if(rows>0){
			for(int row=0; row<rows; row++){
				Widget rowWidget = tblView.getRow(row);
				if(rowWidget instanceof AggregationGridRow){
					AggregationGridRow r = (AggregationGridRow)rowWidget;
					T value = mapper.getData(r.getData()); 
					
					if(value!=null)
						vals.add(value);	
				}
			}
		}
				
		return vals;
	}
	
	class SummaryRenderer extends HTMLPanel{

		InlineLabel label = new InlineLabel();
		public SummaryRenderer(Object value) {
			super("");
			this.add(label);
			
			String text = "";
			if(value instanceof Number){
				text = NumberUtils.CURRENCYFORMAT.format((Number) value);
			}else if(value!=null){
				text = value.toString();
			}
			
			label.setText(text);
			this.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
		}
				
	}

	public void refresh() {
		setColumnConfigs(columnConfigs);
		tblView.clearRows();
		setData(new ArrayList<DataModel>());
	}
	
	public ArrayList<String> getErrors(){
		int rowCount = tblView.getRowCount();
		for(int i=0; i<rowCount; i++){
			Widget rowWidget = tblView.getRow(i);
			AggregationGridRow gridRow = (AggregationGridRow)rowWidget;
			
			ArrayList<String> err = gridRow.getErrors(); 
			if(err!=null){
				return err;
			}
		}
		
		return null;
	}
	
}
