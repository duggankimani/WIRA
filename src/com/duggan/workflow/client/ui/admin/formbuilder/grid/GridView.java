package com.duggan.workflow.client.ui.admin.formbuilder.grid;

import java.util.ArrayList;
import java.util.Collection;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class GridView extends Composite {

	private static GridViewUiBinder uiBinder = GWT
			.create(GridViewUiBinder.class);

	interface GridViewUiBinder extends UiBinder<Widget, GridView> {
	}
	
	@UiField HTMLPanel header;
	@UiField HTMLPanel panelLines;
	@UiField Anchor aNewRecord;
	@UiField SpanElement spnNewRecordHandlerText;
	
	Collection<Field> columnConfigs = new ArrayList<Field>();
	
	public GridView(Collection<Field> columns, Long parentId) {	
		initWidget(uiBinder.createAndBindUi(this));
		setNewRecordHandlerText("Add Row");	
		
		if(columns!=null){
			columnConfigs = columns;		
			for(Field field:columns){
				field.setParentId(parentId);
				header.add(createHeader(field));
			}
		}
		
		init();		
		
	}
	
	private void init() {
		aNewRecord.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DocumentLine line = new DocumentLine();
				createLine(line);
			}
		});
	}

	public void setNewRecordHandlerText(String text){
		spnNewRecordHandlerText.setInnerText(text);
	}
	
	public void setData(Collection<DocumentLine> lines){
		for(DocumentLine line: lines){
			//System.err.println(line);
			createLine(line);
		}
	}
	
	public Collection<DocumentLine> getRecords(){
		Collection<DocumentLine> lines = new ArrayList<DocumentLine>();
		int count = panelLines.getWidgetCount();
		
		for(int i=0; i<count; i++){
			GridRow gridRow = (GridRow)panelLines.getWidget(i);
			DocumentLine line=gridRow.getRecord();
			lines.add(line);
		}
		
		return lines;
	}

	private void createLine(DocumentLine line) {
		GridRow row = new GridRow(columnConfigs, line);	
		panelLines.add(row);
	}

	Widget createHeader(Field field){
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("th generic-header");
		panel.add(new HTML(field.getCaption()));
		return panel;
	}

	public void setReadOnly(boolean readOnly) {
		aNewRecord.setVisible(!readOnly);
		int widgetCount = panelLines.getWidgetCount();
		for(int i=0; i<widgetCount; i++){
			GridRow row = (GridRow)panelLines.getWidget(i);
			row.setReadOnly(readOnly);
		}
	}
}
