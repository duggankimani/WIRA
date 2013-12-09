package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GridLayout extends FieldWidget {
	
	private static GridLayoutUiBinder uiBinder = GWT
			.create(GridLayoutUiBinder.class);

	interface GridLayoutUiBinder extends UiBinder<Widget, GridLayout> {
	}
	

	@UiField Label lblEl;
	@UiField HTMLPanel divControls;
	@UiField Anchor btnAdd;
	
	GridDnD grid = null;
	
	private List<Field> fields = new ArrayList<Field>();
	private final Widget widget;
	
	public GridLayout() {
		super();
		addProperty(new Property(COLUMNPROPERTY, "Column Property", DataType.COLUMNPROPERTY, id));
		addProperty(new Property(COLUMNPROPERTY, "Column Property", DataType.COLUMNPROPERTY, id));
		
		widget= uiBinder.createAndBindUi(this);
		add(widget);
		
		btnAdd.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addColumn();
				grid.repaint();
			}
		});
	}

	@Override
	public void defaultProperties() {
		//Does not have Any default Properties
	}
	
	@Override
	public void activatePopup() {
		super.activatePopup();
		
		divControls.clear();
		if(fields.size()==0)
		for(int i=1;i<=5;i++){
			addColumn();
		}
	
		grid= new GridDnD(fields);
		divControls.add(grid);
		this.getElement().getStyle().setPaddingBottom(20, Unit.PX);
	}
	
	@Override
	public void addShim(int left, int top, int offSetWidth, int offSetHeight) {
		offSetHeight= 125;
		if(popUpActivated){
			top = 60;
			getShim().setPixelSize(offSetWidth, offSetHeight-top);
		}
		super.addShim(left, top,offSetWidth, offSetHeight);
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
		GridLayout layout = new GridLayout();
		layout.setData(fields);
		return layout;
	}

	private void setData(List<Field> fieldz) {
		this.fields = fieldz;
	}

	@Override
	protected DataType getType() {
		return DataType.GRID;
	}

	
	protected void addColumn() {
		Field field = new Field();
		field.setId(System.currentTimeMillis());
		field.setType(DataType.SELECTBASIC);
		field.setCaption("Column "+(fields.size()+1));
		fields.add(field);
	}

}
