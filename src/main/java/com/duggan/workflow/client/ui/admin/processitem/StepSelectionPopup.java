package com.duggan.workflow.client.ui.admin.processitem;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.shared.model.Listable;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.form.Form;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class StepSelectionPopup extends Composite{

	private static StepSelectionPopupUiBinder uiBinder = GWT
			.create(StepSelectionPopupUiBinder.class);

	interface StepSelectionPopupUiBinder extends
			UiBinder<Widget, StepSelectionPopup> {
	}

	@UiField TableView tblView;
	private List<Listable> selected = new ArrayList<Listable>();
	ValueChangeHandler<Boolean> vch = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			DataCheckBox chk = (DataCheckBox)event.getSource();
			if(event.getValue()){
				selected.add(chk.data);
			}else{
				selected.remove(chk.data);
			}
		}
	};
	
	public StepSelectionPopup(List<Form> forms, List<OutputDocument> docs) {
		initWidget(uiBinder.createAndBindUi(this));
		setTable();
		addForms(forms);
		addDocs(docs);
	}

	private void setTable() {
		//tblView.setAutoNumber(true);
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("#", 10.0));
		th.add(new TableHeader("Step Name", 40.0,"title"));
		th.add(new TableHeader("Mode", 10.0));
		th.add(new TableHeader("Condition", 40.0));
				
		tblView.setTableHeaders(th);
	}
	
	
	private void addForms(List<Form> forms) {
		
		for(Form form: forms){
			ListBox list = new ListBox();
			list.addItem(MODE.EDIT.getName(), MODE.EDIT.getName());
			list.addItem(MODE.VIEW.getName(), MODE.VIEW.getName());
			list.addStyleName("input-small");
			TextBox box = new TextBox();
			box.addStyleName("input-small");
			
			tblView.addRow(
					new DataCheckBox(form),
					new InlineLabel(form.getCaption()),
					list, box);
		}
	}
	
	public List<Listable> getSelectedValues(){
		return selected;
	}
	
	private void addDocs(List<OutputDocument> docs) {
		
		for(OutputDocument doc:docs){
			TextBox box = new TextBox();
			box.addStyleName("input-small");
			tblView.addRow(
					new DataCheckBox(doc),
					new InlineLabel(doc.getName()),
					new InlineLabel(), box);
		}
	}
	
	class DataCheckBox extends CheckBox{
		Listable data;
		
		public DataCheckBox(Listable data){
			this.data = data;
			addValueChangeHandler(vch);
		}
	}

}
