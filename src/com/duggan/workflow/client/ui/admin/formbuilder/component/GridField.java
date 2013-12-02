package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author duggan
 * Grid Field
 *  
 * 
 */
public class GridField extends FieldWidget {

	private static GridFieldUiBinder uiBinder = GWT
			.create(GridFieldUiBinder.class);

	interface GridFieldUiBinder extends UiBinder<Widget, GridField> {
	}
	
	private final Widget widget;
	
	@UiField Element lblEl;
	@UiField HorizontalPanel columnPanel;
	
	public GridField() {
		super();		
		widget = uiBinder.createAndBindUi(this);	
		add(widget);
		columnPanel.setBorderWidth(1);
		columnPanel.setHeight("60px");
		
	}

	@Override
	public FieldWidget cloneWidget() {
		return new GridField();
	}

	@Override
	protected DataType getType() {
		return DataType.GRID;
	}

	@Override
	protected void setCaption(String caption) {
		super.setCaption(caption);
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
	}
	
	@Override
	public Value getFieldValue() {
		return super.getFieldValue();
	}

	public HorizontalPanel getColumnPanel() {
		
		return columnPanel;
	}
}
