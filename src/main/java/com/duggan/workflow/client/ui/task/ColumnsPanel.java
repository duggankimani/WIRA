package com.duggan.workflow.client.ui.task;

import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.shared.model.Column;
import com.duggan.workflow.shared.model.Schema;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ColumnsPanel extends Composite {

	private static ColumnsPanelUiBinder uiBinder = GWT
			.create(ColumnsPanelUiBinder.class);

	interface ColumnsPanelUiBinder extends UiBinder<Widget, ColumnsPanel> {
	}
	
	@UiField HTMLPanel accordion;
	private HashMap<String, Column> values =  new HashMap<String, Column>();

	public ColumnsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		accordion.getElement().setId("column-configure");
	}

	public ColumnsPanel(HashMap<String, Column> values, ArrayList<Schema> schemas) {
		this();
		this.values = values;
		for(Schema schema: schemas){
			create(schema);
		}
		
	}
	
	public void create(Schema schema){
		if(schema.getCaption()==null){
			return;
		}
		HTMLPanel accordionGroup = new HTMLPanel("");
		accordionGroup.addStyleName("accordion-group");
		accordion.add(accordionGroup);
		
		final HTMLPanel heading = new HTMLPanel("");
		heading.setStyleName("accordion-heading");
		accordionGroup.add(heading);
		
		final Anchor headerLink = new Anchor();
		headerLink.setHref("#"+schema.getRefId()+"_body");
		headerLink.addStyleName("accordion-toggle");
		headerLink.getElement().setAttribute("data-toggle", "collapse");
		headerLink.getElement().setAttribute("data-parent", "#"+accordion.getElement().getId());
		Element strong = DOM.createElement("strong");
		strong.setInnerText(schema.getCaption().toUpperCase());
		headerLink.getElement().appendChild(strong);
		heading.add(headerLink);
		
		HTMLPanel body = new HTMLPanel("");
		body.addStyleName("accordion-body collapse");
		body.getElement().setId(""+schema.getRefId()+"_body");
		accordionGroup.add(body);
		
		
		HTMLPanel accordionInner = new HTMLPanel("");
		accordionInner.addStyleName("accordion-inner");
		body.add(accordionInner);
		
		FlexTable table = new FlexTable();
		table.addStyleName("table table-condensed table-striped table-highlight cols-table");
		
		int i=0;
		int j=0;
		for(Column col: schema.getColumns()){
			HTMLPanel panel = new HTMLPanel("");
			Checkbox checkbox = new Checkbox(col);
			String text = col.getCaption()==null? col.getName(): col.getCaption();
			if(text ==null){
				continue;
			}
			checkbox.setText(""+text.toUpperCase());
			boolean isContained = values.get(col.getRefId())!=null; 
			checkbox.setValue(isContained);
			checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Checkbox box = (Checkbox)event.getSource();
					Column c = (Column)box.getModel();
					if(event.getValue()){
						values.put(c.getRefId(), c);
					}else{
						values.remove(c.getRefId());
					}
					
				}
			});
			panel.add(checkbox);
			table.setWidget(i, j++, panel);
			if(j>1){
				++i;
				j=0;
			}
		}
		
		if(i%2!=0){
			table.setWidget(i, j, new HTMLPanel(""));
		}
		
		accordionInner.add(table);
	}

	public HashMap<String, Column> getValues() {
		return values;
	}
	
	public void add(Column col){
		values.put(col.getRefId(), col);
	}

}
