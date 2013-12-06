package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class GridLayout extends FieldWidget {
	
	private static GridLayoutUiBinder uiBinder = GWT
			.create(GridLayoutUiBinder.class);

	@UiField LabelElement lblEl;
	@UiField DivElement divControls;
	interface GridLayoutUiBinder extends UiBinder<Widget, GridLayout> {
	}
	
	private final Widget widget;
	public GridLayout() {
		super();
		
		/*
		addProperty(new Property(COLUMNLABEL, "Column Label", DataType.STRING, id));
		addProperty(new Property(COLUMNTYPE, "Column Type", DataType.SELECTBASIC, id));
		*/
		
		addProperty(new Property(COLUMNPROPERTY, "Column Property", DataType.COLUMNPROPERTY, id));
		addProperty(new Property(COLUMNPROPERTY, "Column Property", DataType.COLUMNPROPERTY, id));
		
		widget= uiBinder.createAndBindUi(this);
		add(widget);
	}
	
	@Override
	public void defaultProperties() {
		//Does not have Any default Properties
	}
	
	@Override
	public void activatePopup() {
		super.activatePopup();
		lblEl.addClassName("hidden");
		divControls.getStyle().setMarginLeft(5, Unit.PX);
	}
	
	@Override
	public void activateShimHandler() {
		getShim().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int top = 20;
				int left = 70;
				int arrowPosition = getShim().getAbsoluteTop()-40;
				AppManager.showPropertyPanel(field, getProperties(), top, left,
						arrowPosition);
			}
		});
	}

	@Override
	public FieldWidget cloneWidget() {
		return new GridLayout();
	}

	@Override
	protected DataType getType() {
		return DataType.GRID;
	}

}
