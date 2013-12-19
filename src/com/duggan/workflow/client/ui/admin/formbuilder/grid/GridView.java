package com.duggan.workflow.client.ui.admin.formbuilder.grid;

import java.util.ArrayList;
import java.util.Collection;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
	
	Collection<Field> columnConfigs = new ArrayList<Field>();
	
	public GridView(Collection<Field> columns) {
		initWidget(uiBinder.createAndBindUi(this));
		columnConfigs = columns;
		
		for(Field field:columns){
			header.add(createHeader(field));
		}
	}
	
	public void setData(Collection<DocumentLine> lines){
		for(DocumentLine line: lines){
			GridRow row = new GridRow(columnConfigs, line);
			panelLines.add(row);
		}
	}

	Widget createHeader(Field field){
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("th");
		panel.add(new HTML(field.getCaption()));
		return panel;
	}
}
