package com.duggan.workflow.client.ui.admin.formbuilder.grid;

import java.util.Collection;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.events.EditUserEvent;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
	@UiField HasClickHandlers aEdit;
	@UiField HasClickHandlers aDelete;
	
	DocumentLine line;
	
	public GridRow(Collection<Field> columns, DocumentLine line) {
		initWidget(uiBinder.createAndBindUi(this));
		this.line = line;
		
		initRow();
		
		for(Field col: columns){
			panelRow.add(createCell(col));
		}
		panelRow.add(getActions());
	}

	private void initRow() {
		aEdit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//fireEvent(new EditUserEvent(user));
			}
		});
		
		aDelete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				AppManager.showPopUp("Confirm Delete",new HTMLPanel("Do you want to delete this row?"),
						new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Ok")){
							//delete(line);
							GridRow.this.removeFromParent();
						}
					}

				},"Cancel","Ok");
				
			}
		});
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
