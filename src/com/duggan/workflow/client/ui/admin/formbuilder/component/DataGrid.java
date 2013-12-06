package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.List;

import com.duggan.workflow.shared.model.DocDetail;
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
	private List<DocDetail> values;
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
	
	public List<DocDetail> getValues() {
		return values;
	}

	public void setValues(List<DocDetail> details) {
		for(DocDetail detail: details){
			List<Value> values = detail.getValues();
			++row;
			
		}
	}
	
	@Override
	public Widget asWidget() {
		
		return this;
	}

}
