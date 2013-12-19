package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.admin.formbuilder.grid.GridView;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
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
	
	private final Widget widget;
	
	public GridLayout() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX, id));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		
		widget= uiBinder.createAndBindUi(this);
		add(widget);
		
		btnAdd.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addColumn();
			}
		});
		
	}
	
	@Override
	protected void afterInit() {
		if(showShim){
			showDesignGrid();
		}else{
			showRuntimeGrid();			
		}
	}
	
	/**
	 * Design Grid	
	 */
	private void showDesignGrid() {
		divControls.clear();
		
		grid= new GridDnD(field.getFields()){
			@Override
			protected void save(List<Field> fields) {
				for(Field fld: fields){
					System.err.println(field.getId()+">> "+fld.getPosition()+"::"+fld.getCaption());
				}
				field.setFields(fields);
				GridLayout.this.save();
			}
		};
		
		divControls.add(grid);		
		this.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		this.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		
		
		grid.getAtxtField().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.STRING);
			}
		});
		
		grid.getAlblField().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addColumn();
			}
		});
		
		grid.getAchckBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addColumn();
			}
		});	
		
		
		grid.getAdateBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addColumn();
			}
		});
		
		grid.getaRadioField().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						addColumn();
					}
				});
		grid.getSlctField().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addColumn();
			}
		});

	}

	/**
	 * Runtime View
	 */
	private void showRuntimeGrid() {
		divControls.clear();
		
		GridView view = new GridView(field.getFields());
		
		Collection<DocumentLine> lines=new ArrayList<DocumentLine>();		
		lines.add(new DocumentLine());
		lines.add(new DocumentLine());
		lines.add(new DocumentLine());
		view.setData(lines);
		
		//Draw fields	
		divControls.add(view);
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
		//layout.setField(field);
		return layout;
	}


	@Override
	protected DataType getType() {
		return DataType.GRID;
	}
	
	protected void addColumn() {
		addColumn(DataType.LABEL);
	}
	
	protected void addColumn(DataType type) {
		Field child = new Field();		
		child.setType(type);		
		child.setParentId(field.getId());	
		int pos = field.getFields().size();
		child.setPosition(pos);				
		child.setCaption("Column "+(pos));
		Property prop = new Property(CAPTION, "Label Text", DataType.STRING, id);
		prop.setValue(new StringValue(child.getCaption()));
		
		field.addField(child);		
		save();
	}
	
	@Override
	public void onAfterSave() {
		grid.repaint(field.getFields());		
	}
	
}
