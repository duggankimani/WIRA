package com.duggan.workflow.client.ui.admin.trigger.taskstep;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.events.LoadTriggersEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.duggan.workflow.shared.model.Trigger;
import com.duggan.workflow.shared.requests.GetTriggersRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveTaskStepTriggerRequest;
import com.duggan.workflow.shared.responses.GetTriggersResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.duggan.workflow.client.service.TaskServiceCallback;

public class TriggerTypePanel extends Composite{

	private static TriggerTypePanelUiBinder uiBinder = GWT
			.create(TriggerTypePanelUiBinder.class);

	interface TriggerTypePanelUiBinder extends
			UiBinder<Widget, TriggerTypePanel> {
	}

	@UiField InlineLabel lblName;
	@UiField InlineLabel lblCount;
	@UiField Anchor aAdd;
	
	TaskStepDTO taskStep;
	TreeItem item;
	
	public TriggerTypePanel(TreeItem item, TaskStepDTO clone, int count) {
		initWidget(uiBinder.createAndBindUi(this));
		this.taskStep = clone;
		this.item = item;
		lblName.setText(clone.getTriggerType().display());
		
		setCount(count);
		
		
		aAdd.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				AppContext.getDispatcher().execute(new GetTriggersRequest(), new TaskServiceCallback<GetTriggersResponse>() {
					@Override
					public void processResult(GetTriggersResponse aResponse) {
						final TriggerSelectionPopup popup = new TriggerSelectionPopup(aResponse.getTriggers());
						AppManager.showPopUp("Add Trigger", popup, new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Done")){
									ArrayList<Trigger> selected = popup.getSelectedValues();
									save(selected);
								}
							}
							
						}, "Done", "Cancel");
					}
				});
				
				
			}
		});
	}
	
	private void save(ArrayList<Trigger> selected) {
		MultiRequestAction action = new MultiRequestAction();
		for(Trigger trigger: selected){
			TaskStepTrigger stepTrigger = new TaskStepTrigger();
			stepTrigger.setTrigger(trigger);
			stepTrigger.setTaskStepId(taskStep.getId());
			stepTrigger.setType(taskStep.getTriggerType());
			SaveTaskStepTriggerRequest request = new SaveTaskStepTriggerRequest(stepTrigger);
			action.addRequest(request);
		}
				
		
		AppContext.getDispatcher().execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				AppContext.fireEvent(new LoadTriggersEvent(item));
			}
		});
	}

	public void setCount(int count) {

		if(count!=0){
			lblCount.setText(" ("+count+") ");
		}else{
			lblCount.setText("");
		}
		
	}

}
