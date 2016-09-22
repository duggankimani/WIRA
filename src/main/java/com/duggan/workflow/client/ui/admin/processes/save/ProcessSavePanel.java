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
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetProcessCategoriesRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.client.util.ArrayUtil;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.Listable;
import com.wira.commons.shared.models.UserGroup;

public class ProcessSavePanel extends Composite {

	private static ProcessSavePanelUiBinder uiBinder = GWT
			.create(ProcessSavePanelUiBinder.class);

	interface ProcessSavePanelUiBinder extends
			UiBinder<Widget, ProcessSavePanel> {
	}
	
	@UiField TextField txtIconStyle;
	@UiField TextField txtColor;
	@UiField Card cardProcess;
	@UiField IssuesPanel issues;
	@UiField TextField txtName;
	@UiField TextField txtProcess;
	@UiField Uploader uploader;
	@UiField AutoCompleteField<Listable> lstUserGroups;
	@UiField VerticalPanel currentAttachmentsPanel;
	@UiField InlineLabel lblWarning;
	@UiField DropDownList<ProcessCategory> lstCategories;

	ProcessDef process = null;
	
	public ProcessSavePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		txtColor.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				cardProcess.setBackgroundColor(event.getValue());
			}
		});
		
		txtIconStyle.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				cardProcess.setIconStyle(event.getValue());
			}
		});
	}

	public ProcessSavePanel(Long processDefId) {
		this();
		loadProcess(processDefId);
	}

	public void setValues(Long processDefId,
			String name,String processId,String description, ArrayList<DocumentType> docTypes,
			ArrayList<Attachment> attachments, ProcessCategory category,ArrayList<Listable> userGroups) {
		txtName.setValue(name);
		txtProcess.setValue(processId);
		setProcessId(processDefId);
		
		if(userGroups!=null){
			lstUserGroups.select(userGroups);
		}
		
		lstCategories.setValue(category);
		
		if(attachments!=null){
			setAttachments(attachments);
		}
		
	}

	public ProcessDef getProcess(){
		ProcessDef def = process;
		if(def==null){
			def = new ProcessDef();
		}
		def.setUsersAndGroups(lstUserGroups.getSelectedItems());
		def.setName(txtName.getValue());
		def.setProcessId(txtProcess.getValue());		
		def.setCategory(lstCategories.getValue());
		
		return def;
	}
	
	public boolean isValid(){
		issues.clear();
		boolean isValid = true;
		
		if(isNullOrEmpty(txtName.getValue())){
			issues.addError("Please specify process name");
			isValid=false;
		}
		
		if(isNullOrEmpty(txtProcess.getValue())){
			issues.addError("Please specify Process Id");
			isValid=false;
		}
				
		return isValid;
	}
		
	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	/**
	 * Process Def  Id
	 */
	public void setProcessId(Long id) {
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADCHANGESET);
		context.setContext("processDefId", id+"");
		context.setAccept(ArrayUtil.asList("xml","bpmn","bpmn2","drl","png","jpeg","jpg","gif","svg"));
		uploader.setContext(context);
	}
	
	public void setAttachments(ArrayList<Attachment> attachments){
		currentAttachmentsPanel.clear();
		if(attachments!=null){
			for(Attachment attachment: attachments){
				if(attachment.getName().endsWith("xml") && attachments.size()>1){
					//possible changeset
					UIObject.setVisible(lblWarning.getElement(),true);
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
	
	private void loadProcess(final Long processDefId) {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetProcessCategoriesRequest());
		requests.addRequest(new GetUsersRequest());
		requests.addRequest(new GetGroupsRequest());
		
		if(processDefId!=null){
			requests.addRequest( new GetProcessRequest(processDefId));
		}
		
		AppContext.getDispatcher().execute(requests, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				int i=0;
				
				GetProcessCategoriesResponse response = (GetProcessCategoriesResponse)results.get(i++);
				setCategories(response.getCategories());
				
				GetUsersResponse usersResp = (GetUsersResponse)results.get(i++);
				GetGroupsResponse groupsResp = (GetGroupsResponse)results.get(i++);
				ArrayList<Listable> items = new ArrayList<Listable>();
				for(HTUser user: usersResp.getUsers()){
					items.add(user);
				}
				for(UserGroup group: groupsResp.getGroups()){
					items.add(group);
				}
				setUserGroups(items);
				
				if(processDefId!=null){
					GetProcessResponse result =  (GetProcessResponse)results.get(i++);
					process = result.getProcessDef();
					setValues(process.getId(),
							process.getName(),process.getProcessId(),
							process.getDescription(),
							process.getDocTypes(), process.getFiles(), process.getCategory(),
							process.getUsersAndGroups());
				}
			}
		});		
	}


}
