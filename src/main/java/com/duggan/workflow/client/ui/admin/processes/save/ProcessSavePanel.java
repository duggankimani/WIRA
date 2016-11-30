package com.duggan.workflow.client.ui.admin.processes.save;

import java.util.ArrayList;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.component.AutoCompleteField;
import com.duggan.workflow.client.ui.component.Card;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.events.DeleteAttachmentEvent;
import com.duggan.workflow.shared.events.DeleteAttachmentEvent.DeleteAttachmentHandler;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.ProjectDto;
import com.duggan.workflow.shared.requests.DeleteAttachmentRequest;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetProcessCategoriesRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetProjectsRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.responses.DeleteAttachmentResponse;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.duggan.workflow.shared.responses.GetProjectsResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.wira.commons.client.util.ArrayUtil;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.Listable;
import com.wira.commons.shared.models.UserGroup;

public class ProcessSavePanel extends Composite implements DeleteAttachmentHandler{

	private static ProcessSavePanelUiBinder uiBinder = GWT
			.create(ProcessSavePanelUiBinder.class);

	interface ProcessSavePanelUiBinder extends
			UiBinder<Widget, ProcessSavePanel> {
	}

	@UiField
	TextField txtIconStyle;
	@UiField
	TextField txtColor;
	@UiField
	Card cardProcess;
	@UiField
	IssuesPanel issues;
	@UiField
	TextField txtName;
	@UiField
	TextField txtProcess;
	@UiField
	Anchor aUpload;
	@UiField
	Uploader uploader;
	@UiField
	AutoCompleteField<Listable> lstUserGroups;
	@UiField
	VerticalPanel currentAttachmentsPanel;
	@UiField
	InlineLabel lblWarning;
	@UiField
	DropDownList<ProcessCategory> lstCategories;
	
	@UiField
	DropDownList<ProjectDto> lstProjects;
	
	ArrayList<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	ProcessDef process = null;

	public ProcessSavePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		txtColor.setText(cardProcess.getBackGroundColor());
		txtIconStyle.setText(cardProcess.getIconStyle());
		UIObject.setVisible(lblWarning.getElement(), false);
		
		txtName.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				cardProcess.setDisplay(txtName.getValue());
			}
		});
		txtName.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				cardProcess.setDisplay(txtName.getValue());
			}
		});

		txtColor.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				cardProcess.setBackGroundColor(event.getValue());
			}
		});

		txtIconStyle.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				cardProcess.setIconStyle(event.getValue());
			}
		});

		aUpload.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(!isValid()){
					return;
				}
				AppContext.getDispatcher().execute(
						new SaveProcessRequest(getProcess()),
						new TaskServiceCallback<SaveProcessResponse>() {
							@Override
							public void processResult(
									SaveProcessResponse aResponse) {
								setProcessDef(aResponse.getProcessDef());
							}
						});
			}
		});
	}

	public ProcessSavePanel(Long processDefId) {
		this();
		loadProcess(processDefId);
	}

	public ProcessDef getProcess() {
		ProcessDef def = process;
		if (def == null) {
			def = new ProcessDef();
		}
		def.setUsersAndGroups(lstUserGroups.getSelectedItems());
		def.setName(txtName.getValue());
		ProjectDto project = lstProjects.getValue();
		if(project!=null){
			def.setProject(project.getName());
			def.setProjectVersion(project.getVersion());
			def.setGroupId(project.getGroupId());
		}
		
		def.setProcessId(txtProcess.getValue());
		def.setCategory(lstCategories.getValue());
		def.setBackgroundColor(txtColor.getValue());
		def.setIconStyle(txtIconStyle.getValue());

		return def;
	}

	public boolean isValid() {
		issues.clear();
		boolean isValid = true;

		if (isNullOrEmpty(txtName.getValue())) {
			issues.addError("Please specify process name");
			isValid = false;
		}

		if (isNullOrEmpty(txtProcess.getValue())) {
			issues.addError("Please specify Process Id");
			isValid = false;
		}

		return isValid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	/**
	 * Process Def Id
	 */
	public void setProcessId(Long id) {
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADCHANGESET);
		context.setContext("processDefId", id + "");
		context.setAccept(ArrayUtil.asList("xml", "bpmn", "bpmn2", "drl",
				"png", "jpeg", "jpg", "gif", "svg"));
		uploader.setContext(context);
	}

	public void setAttachments(ArrayList<Attachment> attachments) {
		currentAttachmentsPanel.clear();
		if (attachments != null) {
			for (Attachment attachment : attachments) {
				if (attachment.getName().endsWith("xml")
						&& attachments.size() > 1) {
					// possible changeset
					UIObject.setVisible(lblWarning.getElement(), true);
				}
				ProcessAttachment panel = new ProcessAttachment(attachment);
				currentAttachmentsPanel.add(panel);
			}
		}
	}

	public void setUserGroups(ArrayList<Listable> userGroups) {
		lstUserGroups.addItems(userGroups);
	}

	public void setCategories(ArrayList<ProcessCategory> categories) {
		lstCategories.setItems(categories);
	}
	

	private void setProcessDef(ProcessDef processDef) {
		process = processDef;
		if(processDef.getRefId()!=null){
			uploader.removeStyleName("hide");
			aUpload.addStyleName("hide");
		}
		
		Long processDefId=process.getId();
		String name= process.getName();
		String processId = process.getProcessId();
		
		ArrayList<Attachment> attachments =process.getFiles();
		ProcessCategory category = process.getCategory();
		ArrayList<Listable> userGroups = process.getUsersAndGroups();
		
		txtName.setValue(name);
		cardProcess.setDisplay(name);
		txtProcess.setValue(processId);
		setProcessId(processDefId);

		if (userGroups != null) {
			lstUserGroups.select(userGroups);
		}

		lstCategories.setValue(category);

		if (attachments != null) {
			setAttachments(attachments);
		}
		
		if(processDef.getIconStyle()!=null){
			txtIconStyle.setValue(processDef.getIconStyle());
			cardProcess.setIconStyle(processDef.getIconStyle());
		}
		
		if(processDef.getBackgroundColor()!=null){
			txtColor.setValue(processDef.getBackgroundColor());
			cardProcess.setBackGroundColor(processDef.getBackgroundColor());
		}
		
	}

	private void loadProcess(final Long processDefId) {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetProcessCategoriesRequest());
		requests.addRequest(new GetUsersRequest());
		requests.addRequest(new GetGroupsRequest());

		if (processDefId != null) {
			requests.addRequest(new GetProcessRequest(processDefId));
		}
		requests.addRequest(new GetProjectsRequest());

		AppContext.getDispatcher().execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult results) {
						int i = 0;

						GetProcessCategoriesResponse response = (GetProcessCategoriesResponse) results
								.get(i++);
						setCategories(response.getCategories());

						GetUsersResponse usersResp = (GetUsersResponse) results
								.get(i++);
						GetGroupsResponse groupsResp = (GetGroupsResponse) results
								.get(i++);
						ArrayList<Listable> items = new ArrayList<Listable>();
						for (HTUser user : usersResp.getUsers()) {
							items.add(user);
						}
						for (UserGroup group : groupsResp.getGroups()) {
							items.add(group);
						}
						setUserGroups(items);

						if (processDefId != null) {
							GetProcessResponse result = (GetProcessResponse) results
									.get(i++);
							setProcessDef(result.getProcessDef());
						}
						
						GetProjectsResponse resp = (GetProjectsResponse)results.get(i++);
						bindProjects(resp.getProjects());
					}
				});
	}
	
	protected void bindProjects(ArrayList<ProjectDto> projects) {
		lstProjects.setItems(projects);
		String projectName = process==null? null: process.getProject();
		if(projectName!=null){
			lstProjects.setValueByKey(projectName);
		}
		
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		addRegisteredHandler(DeleteAttachmentEvent.getType(), this);
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		cleanUpEvents();
	}
	

	/**
	 * 
	 * @param type
	 * @param handler
	 */
	public void addRegisteredHandler(Type<? extends EventHandler> type,
			EventHandler handler) {
		@SuppressWarnings("unchecked")
		HandlerRegistration hr = AppContext.getEventBus().addHandler(
				(GwtEvent.Type<EventHandler>) type, handler);
		handlers.add(hr);
	}

	/**
	 * 
	 */
	protected void cleanUpEvents() {
		for (HandlerRegistration hr : handlers) {
			hr.removeHandler();
		}
		handlers.clear();
	}
	
	@Override
	public void onDeleteAttachment(DeleteAttachmentEvent event) {
		Attachment attachment = event.getAttachment();
		AppContext.getDispatcher().execute(new DeleteAttachmentRequest(attachment.getId()),
				new TaskServiceCallback<DeleteAttachmentResponse>() {
					@Override
					public void processResult(DeleteAttachmentResponse result) {
					}
				});
	}

}
