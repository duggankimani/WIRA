package com.duggan.workflow.client.ui.admin.processitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.events.ProcessSelectedEvent;
import com.duggan.workflow.client.ui.events.ProcessSelectedEvent.ProcessSelectedHandler;
import com.duggan.workflow.shared.model.Listable;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.requests.GetTaskNodesRequest;
import com.duggan.workflow.shared.requests.GetTaskStepsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveTaskStepRequest;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.duggan.workflow.shared.responses.GetTaskNodesResponse;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveTaskStepResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class TaskStepPresenter extends
		PresenterWidget<TaskStepPresenter.MyView> implements ProcessSelectedHandler{

	public interface MyView extends View {
		void show(boolean show);
		HasClickHandlers getAddItemActionLink();
		void setTasks(List<TaskNode> values);
		void displaySteps(List<TaskStepDTO> dtos);
		TaskNode getSelectedTask();
		DropDownList<TaskNode> getTasksDropDown();
		void setDeleteHandler(ClickHandler clickHandler);
	}
	
	@Inject DispatchAsync requestHelper;

	ProcessDef processDef;
	TaskNode selected;
	
	List<Form> forms;
	List<OutputDocument> templates;
	
	@Inject
	public TaskStepPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ProcessSelectedEvent.TYPE, this);
		
		getView().setDeleteHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				TaskStepDTO dto = ((TaskStepView.Link)event.getSource()).dto;
				dto.setActive(false);
				
				saveDTOs(Arrays.asList(dto));
			}
		});
		
		getView().getTasksDropDown().addValueChangeHandler(new ValueChangeHandler<TaskNode>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<TaskNode> event) {
				selected = event.getValue();
				reloadSteps();
			}
		});
			
		
		getView().getAddItemActionLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final StepSelectionPopup popup =  new StepSelectionPopup(forms,templates);
				AppManager.showPopUp("Add Step", popup,
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Save")){
									List<Listable> list = popup.getSelectedValues();
									if(!list.isEmpty()){
										saveSelectedSteps(list);
									}
								}
								
							}

						}, "Save","Cancel");
			}
		});
		
		
	}

	public void setProcess(ProcessDef def) {
		this.processDef = def;
	}

	@Override
	public void onProcessSelected(ProcessSelectedEvent event) {
		ProcessDef def = event.getProcessDef();
		if(def.getId().equals(processDef.getId())){
			getView().show(event.isSelected());
			if(event.isSelected()){
				loadForms();
			}
		}
	}
	
	public void loadForms(){
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFormsRequest());
		action.addRequest(new GetOutputDocumentsRequest());
		action.addRequest(new GetTaskNodesRequest(processDef.getProcessId()));
		action.addRequest(new GetTaskStepsRequest(processDef.getProcessId(), null));
				
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				forms = ((GetFormsResponse)results.get(0)).getForms();
				templates = ((GetOutputDocumentsResponse)results.get(1)).getDocuments();
				getView().setTasks(((GetTaskNodesResponse)results.get(2)).getTaskNodes());
				getView().displaySteps(((GetTaskStepsResponse)results.get(3)).getSteps());
			}});
				
	}
	
	void reloadSteps(){
		Long nodeId = selected==null? null: selected.getNodeId();
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetTaskStepsRequest(processDef.getProcessId(), nodeId));
		
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				getView().displaySteps(((GetTaskStepsResponse)results.get(0)).getSteps());
			}});
	}

	private void saveSelectedSteps(List<Listable> list) {
		
		Long nodeId = selected==null? null: selected.getNodeId();
		
		List<TaskStepDTO> dtos = new ArrayList<TaskStepDTO>();
		for(Listable l : list){
			TaskStepDTO dto = new TaskStepDTO();
			dto.setCondition("");
			
			if(l instanceof Form){
				dto.setFormId(((Form)l).getId());
				dto.setMode(MODE.EDIT);
			}else{
				dto.setOutputDocId(((OutputDocument)l).getId());
			}
			
			
			dto.setNodeId(nodeId);
			dto.setProcessDefId(processDef.getId());	
			dtos.add(dto);
		}
		
		assert dtos.size()>0;
		saveDTOs(dtos);
	
	}

	private void saveDTOs(List<TaskStepDTO> dtos) {
		requestHelper.execute(new SaveTaskStepRequest(dtos), new TaskServiceCallback<SaveTaskStepResponse>() {
			@Override
			public void processResult(SaveTaskStepResponse aResponse) {
				List<TaskStepDTO> dtos = aResponse.getList();
				getView().displaySteps(dtos);
			}
		});
	}

}
