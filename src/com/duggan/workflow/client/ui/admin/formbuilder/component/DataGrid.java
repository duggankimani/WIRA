package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.Collection;
import java.util.List;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author duggan
 *
 */
public class DataGrid extends Composite {

	FlexTable table = new FlexTable();
	
	private List<FieldWidget> fields;
	private List<DocumentLine> values;
	int row=0;
		
	public DataGrid(){
		this.initWidget(table);
		table.setWidget(row, 0, new InlineLabel("Col1"));
		table.setWidget(row, 1, new InlineLabel("Col2"));
		table.setWidget(row, 2, new InlineLabel("Col3"));
		table.setWidget(row, 3, new InlineLabel("Col4"));
		table.setBorderWidth(1);
	}
	
	public DataGrid(List<FieldWidget> fields){
		this.fields = fields;
		
		for(FieldWidget fw: fields){
			addField(fw);
		}
	}

	public List<FieldWidget> getFields() {
		return fields;
	}


	public void addField(FieldWidget field){
		field.getField().getCaption();
		table.add(field);
		
	}
	
	public List<DocumentLine> getValues() {
		return values;
	}

	public void setValues(List<DocumentLine> details) {
		for(DocumentLine detail: details){
			Collection<Value> values = detail.getValues().values();
			++row;			
		}
	}
	
	@Override
	public Widget asWidget() {
		
		return this;
	}

}
