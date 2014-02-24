package com.duggan.workflow.client.ui.admin.formbuilder.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.events.DeleteLineEvent;
import com.duggan.workflow.client.ui.events.EditLineEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class GridRow extends Composite {

	private static GridRowUiBinder uiBinder = GWT.create(GridRowUiBinder.class);

	interface GridRowUiBinder extends UiBinder<Widget, GridRow> {
	}

	@UiField HTMLPanel panelRow;
	@UiField HTMLPanel actionPanel;
	//@UiField Anchor aEdit;
	@UiField Anchor aDelete;
	
	DocumentLine line;
	Long detailId=null;
	
	public GridRow(Collection<Field> columns, DocumentLine line) {
		initWidget(uiBinder.createAndBindUi(this));
		this.line = line;
		detailId = line.getId();
		
		if(detailId==null){
			detailId = System.currentTimeMillis();
		}
		
		initRow();
		
		for(Field col: columns){
			Field clone = col.clone(true);
			clone.setDetailId(detailId);
			//System.err.println(clone.getId()+" : "+clone.getParentId()+" : "+clone.getDetailId());
			panelRow.add(createCell(clone));
		}
		panelRow.add(getActions());
	}

	private void initRow() {
		/*aEdit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditLineEvent(getRecord()));
			}
		});*/
		
		aDelete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				AppManager.showPopUp("Confirm Delete",new HTMLPanel("Do you want to delete this row?"),
						new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Ok")){
							AppContext.fireEvent(new DeleteLineEvent(line));
							GridRow.this.removeFromParent();
						}
					}

				},"Cancel","Ok");
				
			}
		});
	}
	
	List<FieldWidget> widgets = new ArrayList<FieldWidget>();
	public Widget createCell(Field field){
		HTMLPanel cell = new HTMLPanel("");
		cell.setStyleName("td");
		
		Value value=line.getValue(field.getName());
		field.setValue(value);
		//System.err.println(field.getName()+">>> "+(value==null? "": value.getValue()));
		FieldWidget fw = FieldWidget.getWidget(field.getType(), field, false);		
		
		Widget widget = fw.getComponent(true);
		assert widget!=null;
		
		cell.add(widget);
		
		widgets.add(fw);
		
		return cell;
	}
	
	public Widget getActions(){
		actionPanel.removeStyleName("hidden");
		return actionPanel;
	}
	
	public DocumentLine getRecord(){
		for(FieldWidget widget: widgets){
			Value val = widget.getFieldValue();
			
			Field field=widget.getField();
			if(field==null){
				continue;
			}
			
			String name =field.getName();
			Value previous = line.getValue(name);
			
			//clear field value
			if(val==null){
				val = previous;
				if(val!=null){
					val.setValue(null);
				}else{
					continue;
				}
			}
			
			val.setKey(name);
			if(previous!=null){
				val.setId(previous.getId());
			}	
			
			line.addValue(name, val);
		}
		
		return line;
	}

	public void setReadOnly(boolean readOnly) {
		UIObject.setVisible(aDelete.getElement(), !readOnly);
		
		for(FieldWidget widget : widgets){
			//Readonly fields are never changed
			widget.setReadOnly(readOnly);
		}
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		for(FieldWidget widget : widgets){
			widget.manualLoad();
		}
	}
	
	protected void onDetach() {
		for(FieldWidget widget : widgets){
			widget.manualUnload();
		}
	};

}
