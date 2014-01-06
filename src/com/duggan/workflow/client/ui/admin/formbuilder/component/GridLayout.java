package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.formbuilder.grid.GridView;
import com.duggan.workflow.client.ui.events.DeleteLineEvent;
import com.duggan.workflow.client.ui.events.DeleteLineEvent.DeleteLineHandler;
import com.duggan.workflow.client.ui.events.EditLineEvent;
import com.duggan.workflow.client.ui.events.EditLineEvent.EditLineHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.duggan.workflow.shared.requests.DeleteLineRequest;
import com.duggan.workflow.shared.responses.DeleteLineResponse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GridLayout extends FieldWidget
implements EditLineHandler{
	
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
		
		AppContext.getEventBus().addHandler(EditLineEvent.TYPE, this);
	}
		
	@Override
	protected void afterInit() {
		if(showShim){
			showDesignGrid();
		}
	}
	
	/**
	 * Design Grid	
	 */
	private void showDesignGrid() {
		divControls.clear();
		field.sortFields();
		grid= new GridDnD(field.getFields()){
			@Override
			protected void save(List<Field> fields) {
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

	private void setLines(Collection<DocumentLine> doclines) {
		if(showShim){
			//design mode
			return;
		}
		
		divControls.clear();
		
		assert field!=null;
		assert field.getFields()!=null;
		
		field.sortFields();
		GridView view = new GridView(field.getFields());
		view.setData(doclines);
		divControls.add(view);
		view.setReadOnly(isReadOnly());
	}

	@Override
	public void addShim(int left, int top, int offSetWidth, int offSetHeight) {
		offSetHeight= 125;
		//if(showShim){
			top = 30;
			getShim().setPixelSize(offSetWidth, offSetHeight-top);
		//}
		//System.err.println("####### top="+top);
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
		assert field!=null;
		
		if(grid==null){
			showDesignGrid();
		}
		assert grid!=null;
		
		grid.repaint(field.getFields());		
	}

	/**
	 * Runtime - Edit line
	 */
	@Override
	public void onEditLine(EditLineEvent event) {
		DocumentLine line = event.getLine();
		HTMLPanel panel = new HTMLPanel("");
		for(String key: line.getValues().keySet()){
			
			Value val = line.getValue(key);
			
			panel.add(new HTML(key+" : "+(val==null? "":val.getValue())));
		}
		
		AppManager.showPopUp("Values", panel, new OnOptionSelected() {
			
			@Override
			public void onSelect(String name) {
								
			}
		}, "OK");
	}
	
	public Collection<DocumentLine> getDocumentLines(){
		Widget child = divControls.getWidget(0);
		if(!(child instanceof GridView)){
			return new ArrayList<DocumentLine>(); 
		}
		GridView gridView = (GridView)child;
		Collection<DocumentLine> documentLines = gridView.getRecords();
		
		return documentLines;
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null){
			//System.err.println("lines");
			@SuppressWarnings("unchecked")
			Collection<DocumentLine> lines = (Collection<DocumentLine>)value;
			setLines(lines);
		}else{
			Collection<DocumentLine> lines=new ArrayList<DocumentLine>();
			lines.add(new DocumentLine());		
			setLines(lines);
		}
	}
	
	@Override
	public Value getFieldValue() {
		
		GridValue value = new GridValue();
		value.setKey(field.getName());
		value.setValue(getDocumentLines());
		
		return value;
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		if(!showShim){
			//not design mode
			
			Widget child = divControls.getWidget(0);
			
			if(child instanceof GridView){
				GridView view = (GridView)child;
				view.setReadOnly(readOnly);
			}
		}
	}
}
