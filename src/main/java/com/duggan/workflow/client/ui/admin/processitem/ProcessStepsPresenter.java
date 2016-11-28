package com.duggan.workflow.client.ui.admin.processitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.notification.NotificationSetupPresenter;
import com.duggan.workflow.client.ui.admin.trigger.taskstep.TaskStepTriggerPresenter;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.events.EditConditionsEvent;
import com.duggan.workflow.client.ui.events.EditConditionsEvent.EditConditionsHandler;
import com.duggan.workflow.shared.events.EditTriggerEvent;
import com.duggan.workflow.shared.events.NotificationCategoryChangeEvent;
import com.duggan.workflow.shared.events.ProcessSelectedEvent;
import com.duggan.workflow.shared.events.ProcessSelectedEvent.ProcessSelectedHandler;
import com.duggan.workflow.shared.events.ProcessingCompletedEvent;
import com.duggan.workflow.shared.events.ProcessingEvent;
import com.duggan.workflow.shared.events.SaveTaskStepEvent;
import com.duggan.workflow.shared.events.SaveTaskStepEvent.SaveTaskStepHandler;
import com.duggan.workflow.shared.model.AssignmentDto;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.NotificationCategory;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.GetAssignmentRequest;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.requests.GetTaskNodesRequest;
import com.duggan.workflow.shared.requests.GetTaskStepsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveAssignmentRequest;
import com.duggan.workflow.shared.requests.SaveTaskStepRequest;
import com.duggan.workflow.shared.responses.GetAssignmentResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.duggan.workflow.shared.responses.GetTaskNodesResponse;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveAssignmentResponse;
import com.duggan.workflow.shared.responses.SaveTaskStepResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.commons.client.util.ArrayUtil;
import com.wira.commons.shared.models.Listable;

public class ProcessStepsPresenter extends
		PresenterWidget<ProcessStepsPresenter.MyView> implements
		ProcessSelectedHandler, SaveTaskStepHandler, EditConditionsHandler {

	public interface MyView extends View {
		HasClickHandlers getAddItemActionLink();

		void setTasks(ArrayList<TaskNode> values);

		void displaySteps(ArrayList<TaskStepDTO> dtos);

		TaskNode getSelectedTask();

		DropDownList<TaskNode> getTasksDropDown();

		void setDeleteHandler(ClickHandler clickHandler);

		void setType(int tabNo);

		HasClickHandlers getTriggersTabLink();

		HasClickHandlers getStepsTabLink();

		HasClickHandlers getAddTriggerLink();

		HasClickHandlers getNotificationsTabLink();
		
		AssignmentDto getAssignment();

		DropDownList<NotificationCategory> getNotificationCategoryDropdown();

		void load();

		void setTaskAssignee(String actorId, String groupId);

		void setAssignmentFunction(AssignmentDto assignment);

		void addAssignmentValueChangeHandler(
				ValueChangeHandler<Boolean> valueChangeHandler);
	}

	@Inject
	DispatchAsync requestHelper;

	ProcessDef processDef;
	TaskNode selected;

	ArrayList<Form> forms;
	ArrayList<OutputDocument> templates;

	@Inject
	TaskStepTriggerPresenter presenter;
	@Inject
	NotificationSetupPresenter notificationsPresenter;

	protected AssignmentDto assignment;

	public static final Object TRIGGER_SLOT = new Object();
	public static final Object NOTIFICATIONS_SLOT = new Object();

	@Inject
	public ProcessStepsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().load();
		addRegisteredHandler(ProcessSelectedEvent.TYPE, this);
		addRegisteredHandler(SaveTaskStepEvent.TYPE, this);
		addRegisteredHandler(EditConditionsEvent.getType(), this);

		getView().addAssignmentValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AssignmentDto dto = getView().getAssignment();
				
				if(selected==null){
					return;
				}
			
				dto.setNodeId(selected.getNodeId());
				dto.setProcessRefId(processDef.getRefId());
				dto.setRefId(assignment==null? null : assignment.getRefId());
				dto.setTaskName(selected.getName());
				
				save(dto);
			}
		});
		
		getView().setDeleteHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				TaskStepDTO dto = ((ProcessStepsView.Link) event.getSource()).dto;
				dto.setActive(false);
				saveDTOs(ArrayUtil.asList(dto));
			}
		});

		getView().getTasksDropDown().addValueChangeHandler(
				new ValueChangeHandler<TaskNode>() {

					@Override
					public void onValueChange(ValueChangeEvent<TaskNode> event) {
						selected = event.getValue();

						if (selected == null) {
							getView().setTaskAssignee(null, null);
						} else {
							getView().setTaskAssignee(selected.getActorId(),
									selected.getGroupId());
						}

						reloadSteps();
					}
				});

		getView().getAddItemActionLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final StepSelectionPopup popup = new StepSelectionPopup(forms,
						templates);
				AppManager.showPopUp("Add Step", popup, new OnOptionSelected() {

					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							ArrayList<Listable> selectedValues = popup.getSelectedValues();
							if (!selectedValues.isEmpty()) {
								saveSelectedSteps(selectedValues);
							}
						}

					}

				}, "Save", "Cancel");
			}
		});

		getView().getTriggersTabLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setInSlot(TRIGGER_SLOT, presenter);
			}
		});

		getView().getNotificationsTabLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				notificationsPresenter.setProcessDefId(processDef.getId());
				setInSlot(NOTIFICATIONS_SLOT, notificationsPresenter);
				loadNotificationTemplate(getView()
						.getNotificationCategoryDropdown().getValue(),
						selected == null ? null : selected.getNodeId(),
						selected == null ? null : selected.getName(),
						processDef.getId());
			}
		});

		getView().getAddTriggerLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditTriggerEvent(null));
			}
		});

		getView().getNotificationCategoryDropdown().addValueChangeHandler(
				new ValueChangeHandler<NotificationCategory>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<NotificationCategory> event) {
						Long nodeId = selected == null ? null : selected
								.getNodeId();
						String stepName = selected == null ? null : selected
								.getName();
						loadNotificationTemplate(event.getValue(), nodeId,
								stepName, processDef.getId());
					}
				});
	}

	protected void save(AssignmentDto dto) {
		SaveAssignmentRequest req = new SaveAssignmentRequest(dto);
		fireEvent(new ProcessingEvent("Saving assignment..."));
		requestHelper.execute(req, new TaskServiceCallback<SaveAssignmentResponse>(){
			@Override
			public void processResult(SaveAssignmentResponse aResponse) {
				if(selected!=null){
					assignment = aResponse.getAssignment();
					getView().setAssignmentFunction(assignment);
				}
				fireEvent(new ProcessingCompletedEvent());
			}
		});
	}

	protected void loadNotificationTemplate(
			NotificationCategory notificationCategory, Long nodeId,
			String stepName, Long processDefId) {
		fireEvent(new NotificationCategoryChangeEvent(
				notificationCategory == null ? NotificationCategory.EMAILNOTIFICATION
						: notificationCategory, nodeId, stepName, processDefId));
	}

	public void setProcess(ProcessDef def) {
		this.processDef = def;
		getView().setTaskAssignee(null, null);
	}

	@Override
	public void onProcessSelected(ProcessSelectedEvent event) {
		ProcessDef def = event.getProcessDef();
		if (def.getId().equals(processDef.getId())) {
			if (event.isSelected()) {
				load();
			}
		}
	}

	public void load() {
		getView().getTasksDropDown().setNullText(
				"--" + processDef.getName() + "--");
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetFormsRequest(processDef.getRefId()));
		action.addRequest(new GetOutputDocumentsRequest(processDef.getRefId()));
		action.addRequest(new GetTaskNodesRequest(processDef.getProcessId()));
		action.addRequest(new GetTaskStepsRequest(processDef.getProcessId(),
				null));

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult results) {
						forms = ((GetFormsResponse) results.get(0)).getForms();
						sort(forms);
						templates = ((GetOutputDocumentsResponse) results
								.get(1)).getDocuments();
						getView().setTasks(
								((GetTaskNodesResponse) results.get(2))
										.getTaskNodes());
						bindSteps(((GetTaskStepsResponse) results.get(3))
								.getSteps());
					}
				});

	}

	protected void sort(ArrayList<Form> forms) {
		Collections.sort(forms, new Comparator<Form>() { 
			@Override
			public int compare(Form o1, Form o2) {
				return o1.getCaption().compareTo(o2.getCaption());
			}
		});
	}

	void bindSteps(ArrayList<TaskStepDTO> dtos) {
		getView().displaySteps(dtos);
		presenter.setTaskSteps(dtos);
	}

	void reloadSteps(){
		Long nodeId = selected==null? null: selected.getNodeId();
		assignment = null;
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetTaskStepsRequest(processDef.getProcessId(), nodeId));
		
		if(selected!=null){
			action.addRequest(new GetAssignmentRequest(processDef.getRefId(),selected.getNodeId()));
		}
		
		loadNotificationTemplate(
				getView().getNotificationCategoryDropdown().getValue(), 
				nodeId, selected==null? null : selected.getName(), processDef.getId());
		
		requestHelper.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				bindSteps(((GetTaskStepsResponse)results.get(0)).getSteps());
				
				if(selected!=null){
					assignment = ((GetAssignmentResponse)results.get(1)).getAssignment();
					getView().setAssignmentFunction(assignment);
				}
			}});
	}

	private void saveSelectedSteps(ArrayList<Listable> ArrayList) {

		Long nodeId = selected == null ? null : selected.getNodeId();

		ArrayList<TaskStepDTO> dtos = new ArrayList<TaskStepDTO>();
		for (Listable l : ArrayList) {
			TaskStepDTO dto = new TaskStepDTO();
			dto.setCondition("");

			if (l instanceof Form) {
				dto.setFormRefId(((Form) l).getRefId());
				dto.setMode(MODE.EDIT);
			} else {
				dto.setOutputDocId(((OutputDocument) l).getId());
			}

			dto.setNodeId(nodeId);
			dto.setProcessDefId(processDef.getId());
			dto.setProcessRefId(processDef.getRefId());
			dtos.add(dto);
		}

		assert dtos.size() > 0;
		saveDTOs(dtos);

	}

	private void saveDTOs(ArrayList<TaskStepDTO> dtos) {
		fireEvent(new ProcessingEvent("Saving Task Steps..."));
		requestHelper.execute(new SaveTaskStepRequest(dtos),
				new TaskServiceCallback<SaveTaskStepResponse>() {
					@Override
					public void processResult(SaveTaskStepResponse aResponse) {
						ArrayList<TaskStepDTO> dtos = aResponse.getList();
						bindSteps(dtos);
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	@Override
	public void onSaveTaskStep(SaveTaskStepEvent event) {
		saveDTOs(ArrayUtil.asList(event.getTaskStepDTO()));
	}

	@Override
	public void onEditConditions(EditConditionsEvent event) {
		final TaskStepDTO dto = event.getTaskStepDto();
		if(dto==null){
			return;
		}
		if(!this.isVisible()){
			/*
			 * Duggan - 25/07/2016 - Hack to avoid multiple presenters handling this event.
			 *  - 2 ProcessStepsPresenter instances may be bound {see @ProcessPresenter.prepareOnRequest}
			 *  to display  Process Preview & Process Configs. The former also handles this event resulting to multiple 
			 *  popups in this method. To handle this, we can use the currently visible presenter. 
			 */
			return;
		}
		
		long taskNodeId = selected==null? -1 : selected.getNodeId();
		long nodeId = dto.getNodeId()==null? -1 : dto.getNodeId();
		if(taskNodeId!=nodeId){
			return; //Belongs to a different ProcessStepsPresenter instance;
		}
		
		
		String stepName = dto.getFormName()==null? dto.getOutputDocName(): dto.getFormName();
		final TextField txtField = new TextField();
		txtField.setValue(dto.getCondition());
		txtField.addStyleName("input-xlarge");
		AppManager.showPopUp(stepName+" Conditions", txtField, new OnOptionSelected() {
			
			@Override
			public void onSelect(String name) {
				if(name.equals("OK")){
					dto.setCondition(txtField.getValue());
					saveDTOs(ArrayUtil.asList(dto));
				}
				
			}
		}, "Cancel", "OK");
	}

}
