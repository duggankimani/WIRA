package com.duggan.workflow.client.ui.admin.trigger.taskstep;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.shared.model.Trigger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TriggerSelectionPopup extends Composite{

	private static StepSelectionPopupUiBinder uiBinder = GWT
			.create(StepSelectionPopupUiBinder.class);

	interface StepSelectionPopupUiBinder extends
			UiBinder<Widget, TriggerSelectionPopup> {
	}

	@UiField TableView tblView;
	private ArrayList<Trigger> selected = new ArrayList<Trigger>();
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
	
	public TriggerSelectionPopup(ArrayList<Trigger> triggers) {
		initWidget(uiBinder.createAndBindUi(this));
		setTable();
		addtriggers(triggers);
	}

	private void setTable() {
		//tblView.setAutoNumber(true);
		ArrayList<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("#", 10.0));
		th.add(new TableHeader("Trigger Name", 90.0,"title"));
				
		tblView.setTableHeaders(th);
	}
	
	
	public ArrayList<Trigger> getSelectedValues(){
		return selected;
	}
	
	private void addtriggers(ArrayList<Trigger> triggers) {
		
		for(Trigger trigger:triggers){
			TextBox box = new TextBox();
			box.addStyleName("input-small");
			tblView.addRow(
					new DataCheckBox(trigger),
					new InlineLabel(trigger.getName()));
		}
	}
	
	class DataCheckBox extends CheckBox{
		Trigger data;
		
		public DataCheckBox(Trigger data){
			this.data = data;
			addValueChangeHandler(vch);
		}
	}

}
