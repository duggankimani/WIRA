package com.duggan.workflow.client.ui.admin.formbuilder.grid;

import java.util.Collection;

import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class GridRow extends Composite {

	private static GridRowUiBinder uiBinder = GWT.create(GridRowUiBinder.class);

	interface GridRowUiBinder extends UiBinder<Widget, GridRow> {
	}

	@UiField HTMLPanel panelRow;
	@UiField HTMLPanel actionPanel;
	DocumentLine line;
	
	public GridRow(Collection<Field> columns, DocumentLine line) {
		initWidget(uiBinder.createAndBindUi(this));
		this.line = line;
		
		initRow(columns);
	}

	private void initRow(Collection<Field> columns) {
		for(Field col: columns){
			panelRow.add(createCell(col));
		}
		panelRow.add(getActions());
	}
	
	public Widget createCell(Field field){
		HTMLPanel cell = new HTMLPanel("");
		cell.setStyleName("td");
		
		FieldWidget fw = FieldWidget.getWidget(field.getType(), field, false);
		
		cell.add(fw.getComponent(true));
		
		return cell;
	}
	
	public Widget getActions(){
		actionPanel.removeStyleName("hidden");
		return actionPanel;
	}

}
