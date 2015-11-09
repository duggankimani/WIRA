package com.duggan.workflow.client.ui.admin.notification;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.HTMLEditor;
import com.duggan.workflow.client.ui.events.NotificationCategoryChangeEvent;
import com.duggan.workflow.client.ui.events.NotificationCategoryChangeEvent.NoticationCategoryChangeHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.NotificationCategory;
import com.duggan.workflow.shared.model.TaskNotification;
import com.duggan.workflow.shared.requests.DeleteNotificationTemplateRequest;
import com.duggan.workflow.shared.requests.GetNotificationTemplateRequest;
import com.duggan.workflow.shared.requests.SaveNotificationTemplateRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetNotificationTemplateResult;
import com.duggan.workflow.shared.responses.SaveNotificationTemplateResult;
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

public class NotificationSetupPresenter extends
		PresenterWidget<NotificationSetupPresenter.INotificationSetupView>
implements NoticationCategoryChangeHandler{

	public interface INotificationSetupView extends View {
		void setNotificationCategory(NotificationCategory category);
		TaskNotification getNotificationTemplate();
		HasClickHandlers getSaveButton();
		void setNotification(TaskNotification notification);
		DropDownList<Actions> getActionsDropdown();
		void clear();
		HasClickHandlers getHTMLEditorLink();
		String getHtmlNotification();
		void setHTMLNotification(String text);
		HasClickHandlers getDeleteButton();
	}

	private Long id;
	private Long nodeId;
	private String stepName;
	private Long processDefId;
	private NotificationCategory category;
	@Inject DispatchAsync requestHelper;

	@Inject
	public NotificationSetupPresenter(final EventBus eventBus, final INotificationSetupView view) {
		super(eventBus, view);
	}
	
	@Override
	public void bind() {
		super.bind();
		addRegisteredHandler(NotificationCategoryChangeEvent.TYPE, this);
		getView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				saveTemplate();
				
			}
		});
		
		getView().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deleteTemplate();
			}
		});
		
		getView().getActionsDropdown().addValueChangeHandler(new ValueChangeHandler<Actions>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Actions> event) {
				Actions action = event.getValue();
				if(action==null){
					action = Actions.CREATE;
				}
				selectionChange(category, stepName, nodeId, processDefId, action);
			}
		});
		
		getView().getHTMLEditorLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final HTMLEditor editor = new HTMLEditor(getView().getHtmlNotification());
				AppManager.showPopUp("Edit HTML",editor, "htmlEditorPopup" , new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Save")){
							getView().setHTMLNotification(editor.getText());
							saveTemplate();
						}
					}
				}, "Save", "Cancel");
			}
		});
	}

	protected void saveTemplate() {
		TaskNotification notification = getView().getNotificationTemplate();
		notification.setId(id);
		notification.setNodeId(nodeId);
		notification.setStepName(stepName);
		notification.setProcessDefId(processDefId);
		notification.setCategory(category);
		assert category!=null;
		saveNotificationTemplate(notification);
	}
	
	protected void deleteTemplate(){
		
		if(id==null)
			return;
		
		requestHelper.execute(new DeleteNotificationTemplateRequest(id),
				new TaskServiceCallback<BaseResponse>() {
			@Override
			public void processResult(BaseResponse aResponse) {
				getView().clear();
				id=null;
			}
		});
	}

	protected void saveNotificationTemplate(TaskNotification notification) {
		
		fireEvent(new ProcessingEvent("Loading..."));
		requestHelper.execute(new SaveNotificationTemplateRequest(notification),
			new TaskServiceCallback<SaveNotificationTemplateResult>() {
			@Override
			public void processResult(
					SaveNotificationTemplateResult aResponse) {
				setTemplate(aResponse.getNotification());
				fireEvent(new ProcessingCompletedEvent());
			}
		});
	}

	protected void setTemplate(TaskNotification notification) {
		getView().clear();
		if(notification.getId()==null){
			return;
		}
		id=notification.getId();
		nodeId = notification.getNodeId();
		processDefId = notification.getProcessDefId();
		category = notification.getCategory();
		getView().setNotification(notification);
	}

	@Override
	public void onNoticationCategoryChange(NotificationCategoryChangeEvent event) {
		if(!event.getProcessDefId().equals(processDefId)){
			return;
		}
		
		category = event.getCategory();
		stepName = event.getStepName();
		nodeId = event.getNodeId();
		
		getView().setNotificationCategory(category);
		selectionChange(category,event.getStepName(),event.getNodeId(),event.getProcessDefId(),
				Actions.CREATE);
	}

	private void selectionChange(NotificationCategory category,
			String stepName,Long nodeId, Long processDefId,Actions action) {
		
		fireEvent(new ProcessingEvent("Loading..."));
		requestHelper.execute(new GetNotificationTemplateRequest(category,
				stepName,nodeId,processDefId,action), 
				new TaskServiceCallback<GetNotificationTemplateResult>() {
			@Override
			public void processResult(
					GetNotificationTemplateResult aResponse) {
				fireEvent(new ProcessingCompletedEvent());
				setTemplate(aResponse.getNotification());
			}
		});
		
	}

	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}

}
