package com.duggan.workflow.client.ui.admin.trigger.taskstep;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.events.EditTriggerEvent;
import com.duggan.workflow.client.ui.events.LoadTriggersEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveTaskStepTriggerRequest;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class TriggerItemPanel extends Composite {

	private static TriggerPanelUiBinder uiBinder = GWT
			.create(TriggerPanelUiBinder.class);

	interface TriggerPanelUiBinder extends UiBinder<Widget, TriggerItemPanel> {
	}

	@UiField InlineLabel lblName;
	@UiField Anchor aDelete;
	@UiField Anchor aEdit;
	@UiField Element spnCondition;
	@UiField Anchor aEditCondition;
	TaskStepTrigger trigger;
	
	TreeItem item;
	
	public TriggerItemPanel(TreeItem parentItem, TaskStepTrigger aTrigger){
		this(aTrigger.getTrigger().getName());
		this.trigger = aTrigger;
		this.item = parentItem;
		spnCondition.setInnerText(aTrigger.getCondition());
		
		aDelete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delete();
			}
		});
		
		aEdit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditTriggerEvent(item,trigger.getTrigger()));
			}
		});
		
		aEditCondition.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditTriggerEvent(item,trigger));
			}
		});
	}

	protected void delete() {
		AppManager.showPopUp("Delete Trigger", "Delete '"+trigger.getTrigger().getName()+"'?",
				new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if(name.equals("Delete")){
							MultiRequestAction action = new MultiRequestAction();
							trigger.setActive(false);
							SaveTaskStepTriggerRequest request = new SaveTaskStepTriggerRequest(trigger);
							action.addRequest(request);
							
							AppContext.getDispatcher().execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
								@Override
								public void processResult(MultiRequestActionResult aResponse) {
									AppContext.fireEvent(new LoadTriggersEvent(item));
								}
							});
						}
					}
				}, "Cancel","Delete");
	}

	public TriggerItemPanel(String name) {
		initWidget(uiBinder.createAndBindUi(this));
		lblName.setText(name);
	}

	public TreeItem getItem() {
		return item;
	}

}
