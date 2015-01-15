package com.duggan.workflow.client.ui.tasklistitem;

import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.events.AfterAttachmentReloadedEvent;
import com.duggan.workflow.client.ui.events.AfterAttachmentReloadedEvent.AfterAttachmentReloadedHandler;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent.AfterDocumentLoadHandler;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.AfterSearchEvent;
import com.duggan.workflow.client.ui.events.AfterSearchEvent.AfterSearchHandler;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent.CompleteDocumentHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.duggan.workflow.client.ui.events.ExecTaskEvent;
import com.duggan.workflow.client.ui.events.ExecTaskEvent.ExecTaskHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.WorkflowProcessEvent;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * This class displays a Task or a Document 
 * 
 * @author duggan
 *
 */
public class TaskItemPresenter extends
		PresenterWidget<TaskItemPresenter.ITaskItemView> 
	implements DocumentSelectionHandler, AfterDocumentLoadHandler, 
	CompleteDocumentHandler, ExecTaskHandler, AfterAttachmentReloadedHandler, AfterSearchHandler{

	public interface ITaskItemView extends View {
		void bind(Doc summaryTask);
		HasClickHandlers getClaimLink();
		HasClickHandlers getStartLink();
		HasClickHandlers getSuspendLink();
		HasClickHandlers getResumeLink();
		HasClickHandlers getCompleteLink();
		HasClickHandlers getDelegateLink();
		HasClickHandlers getRevokeLink();
		HasClickHandlers getStopLink();
		HasClickHandlers getForwardLink();
		HasClickHandlers getViewLink();
		HasClickHandlers getSubmitForApprovalLink();
		HasClickHandlers getApproveLink();
		HasClickHandlers getRejectLink();
		FocusPanel getFocusContainer();
		void setSelected(boolean selected);
		void setMiniDocumentActions(boolean status);
		void setTask(boolean isTask);
		void showAttachmentIcon(boolean hasAttachment);
		void highlight(String txt);
		void highlight(String subject, String description);

	}

	Doc task;
	
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
	ExecuteWorkflow workflow;
	
	@Inject
	public TaskItemPresenter(final EventBus eventBus, final ITaskItemView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(CompleteDocumentEvent.TYPE, this);
		addRegisteredHandler(AfterDocumentLoadEvent.TYPE, this);
		addRegisteredHandler(DocumentSelectionEvent.TYPE, this);
		addRegisteredHandler(ExecTaskEvent.TYPE, this);
		addRegisteredHandler(AfterAttachmentReloadedEvent.TYPE, this);
		addRegisteredHandler(AfterSearchEvent.TYPE, this);
//		
		workflow = new ExecuteWorkflow(0l, AppContext.getUserId(), Actions.START);
		 
		getView().getFocusContainer().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(task instanceof Document){
					Document doc = (Document)task;
					fireEvent(new DocumentSelectionEvent(doc.getId(),null, DocMode.READWRITE));
				}else{
					Long taskId = ((HTSummary)task).getId();
					Long docId = ((HTSummary)task).getDocumentRef();
					fireEvent(new DocumentSelectionEvent(docId, taskId, DocMode.READ));
				}
			}
		});		
		getView().getFocusContainer().addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				getView().setMiniDocumentActions(true);
			}
		});
		
		getView().getFocusContainer().addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				getView().setMiniDocumentActions(false);
			}
		});
		
		getView().getSubmitForApprovalLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dispatcher.execute(new ApprovalRequest(AppContext.getUserId(), (Document)task), 
						new TaskServiceCallback<ApprovalRequestResult>(){
					@Override
					public void processResult(ApprovalRequestResult result) {						
						PlaceRequest request = new PlaceRequest.Builder().nameToken("home")
								.with("type", TaskType.INBOX.getURL()).build();
//								new PlaceRequest("home").
//								with("type", TaskType.INBOX.getURL());
						
						placeManager.revealPlace(request);
						
					}
				});				
			}
		});


		getView().getClaimLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.CLAIM);
			}
		});
		
		getView().getStartLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.START);
			}
		});
		
		getView().getSuspendLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.SUSPEND);
			}
		});
		
		getView().getResumeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.RESUME);
			}
		});
		
		getView().getCompleteLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.COMPLETE);
			}
		});
		
		getView().getDelegateLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.DELEGATE);
			}
		});
		
		getView().getRevokeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				submitRequest(Actions.REVOKE);
			}
		});
		
		getView().getStopLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.STOP);
			}
		});
		
		getView().getForwardLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.FORWARD);
			}
		});
		
		getView().getApproveLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HTMLPanel panel = new HTMLPanel("");
				panel.add(new InlineLabel("Do you want to reject this request?"));
				TextArea area = new TextArea();
				area.setPlaceholder("Rejection reason or comments");
				panel.add(area);
				
				AppManager.showPopUp("Rejection Comments", panel, new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Reject")){
							//create comment
							completeDocument(true);
						}						
					}
				}, "Reject", "Cancel");				
			}
		});
		
		getView().getRejectLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				HTMLPanel panel = new HTMLPanel("");
				panel.add(new InlineLabel("Do you want to reject this request?"));
				TextArea area = new TextArea();
				area.setPlaceholder("Rejection reason or comments");
				panel.add(area);
				
				AppManager.showPopUp("Rejection Comments", panel, new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Reject")){
							//create comment
							completeDocument(false);
						}						
					}
				}, "Reject", "Cancel");				
			}
		});
		
	}
	
	void completeDocument(boolean approved){
		Map<String, Value> arguments = new HashMap<String, Value>();	
		arguments.put("isApproved", new BooleanValue(approved));
		arguments.put("documentId", new LongValue(((HTSummary)task).getDocumentRef()));
		submitRequest(Actions.COMPLETE, arguments);
	}

	protected void submitRequest(Actions action){
		submitRequest(action, null);
	}
	
	protected void submitRequest(final Actions action, final Map<String, Value> values) {
		
		//String docUrl = (GWT.getModuleBaseURL()+"/search?");
		//values.put("DocumentURL", new StringValue(docUrl));
		fireEvent(new ProcessingEvent());
		workflow.setAction(action);
		workflow.setValues(values);
				
		if(task instanceof Document){
			workflow.setTaskId(new Long((Integer)task.getId()));
		}			
		else
			workflow.setTaskId((Long)task.getId());
		
		if(action==Actions.DELEGATE){
			delegate(workflow,action, values);
			return;
		}				
		
		dispatcher.execute(workflow, new TaskServiceCallback<ExecuteWorkflowResult>() {
			
			@Override
			public void processResult(ExecuteWorkflowResult result) {
				Doc doc = result.getDocument();
				
				//refresh list
				fireEvent(new ProcessingCompletedEvent());
				
				if(action==Actions.COMPLETE || action==Actions.SUSPEND || action==Actions.RESUME){
					String out = "";
					if(action==Actions.COMPLETE){
						Boolean isApproved = null;
						if(values!=null){
							Value value = values.get("isApproved");
							if(value!=null){
								isApproved= value.getValue()==null? null : (Boolean)(value.getValue());
							}
						}
						
						out = isApproved==null? "Review completed for ":
								isApproved? "You have Approved ": "You have Rejected ";
					}else if(action==Actions.SUSPEND){
						out = "You have suspended ";
					}else if(action==Actions.RESUME){
						out = "You have resumed ";
					}
					
					//removeFromParent();
					fireEvent(new AfterSaveEvent());
					//reload((HTSummary)result.getDocument());
					fireEvent(new WorkflowProcessEvent(doc.getCaseNo(),out,doc));
				}else{
					//fireEvent(new ReloadEvent());
					reload((HTSummary)result.getDocument());
				}
				//	
			}
		});
	}

	private void delegate(ExecuteWorkflow execWorkflow,Actions action, Map<String, Value> values) {
		Notification notification = new Notification();
		notification.setApproverAction(ApproverAction.DELEGATE);
		HTSummary summary = (HTSummary)task;
		notification.setDocumentId(summary.getDocumentRef());
		//notification.setDocumentType(summary.getDocStatus());
		//notification.setOwner(summary.getOwner());
		notification.setProcessInstanceId(summary.getProcessInstanceId());
		notification.setNotificationType(NotificationType.TASKDELEGATED);
		notification.setRead(false);
		notification.setSubject(summary.getCaseNo());
		
		HTUser user = new HTUser((String)values.get("targetUserId").getValue());
		notification.setTargetUserId(user);
		//Delegation Recipient Notification
		SaveNotificationRequest saveNoteRequest = new SaveNotificationRequest(notification);
		
//		Notification delegatorNote = notification.clone();
//		delegatorNote.setTargetUserId(AppContext.getContextUser().getUserId());
//		SaveNotificationRequest saveNoteRequest2 = new SaveNotificationRequest(delegatorNote);
		
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(execWorkflow);
		requests.addRequest(saveNoteRequest);
		//requests.addRequest(saveNoteRequest2);
		
		dispatcher.execute(requests, new TaskServiceCallback<MultiRequestActionResult>() {
			
			@Override
			public void processResult(MultiRequestActionResult results) {
				//refresh list
				fireEvent(new ProcessingCompletedEvent());			
				
				ExecuteWorkflowResult result = (ExecuteWorkflowResult)results.get(0);
				//fireEvent(new ReloadEvent());
				reload((HTSummary)result.getDocument());
				
			}
		});
		
	}

	protected void reload(HTSummary summary) {
		setDocSummary(summary);
		if(task instanceof Document){
			Document doc = (Document)task;
			fireEvent(new DocumentSelectionEvent(doc.getId(), null, DocMode.READWRITE));
		}else{
			Long taskId = ((HTSummary)task).getId();
			fireEvent(new DocumentSelectionEvent((Long)task.getId(), taskId, DocMode.READ));
		}
		
	}

	protected void removeFromParent() {
		unbind();
		this.getView().asWidget().removeFromParent();
	}

	public void setDocSummary(Doc summaryTask) {
		this.task = summaryTask;
		if(summaryTask!=null){
			getView().bind(summaryTask);
		}
		
		if(task instanceof HTSummary){
			getView().setTask(true);
		}else{
			getView().setTask(false);
		}
	}
	
	@Override
	public void onDocumentSelection(DocumentSelectionEvent event) {
		Long documentId = event.getDocumentId();
		Long taskId = event.getTaskId();
		
		if((task instanceof  Document) && taskId==null){
			Document doc = (Document)task;
			if(doc.getId()!=documentId){
				getView().setSelected(false);
			}else{
				getView().setSelected(true);
			}
		}else if(task instanceof HTSummary){
			Long tId = (Long)task.getId();
			
			if(taskId==null || !taskId.equals(tId)){
				getView().setSelected(false);
			}else{
				getView().setSelected(true);
			}
			
		}else{
			getView().setSelected(false);
		}
	}

	/**
	 * TODO: Review use of documentId here
	 */
	@Override
	public void onAfterDocumentLoad(AfterDocumentLoadEvent event) {
		if(task instanceof Document){
			return;
		}
		
		HTSummary summary = (HTSummary)task;
		
		if(summary.getId().equals(event.getTaskId())){
			event.setValidActions(summary.getStatus().getValidActions());
		}
		
//		if(summary.getDocumentRef()!=null && summary.getDocumentRef()==event.getDocumentId()){
//			event.setValidActions(summary.getStatus().getValidActions());
//		}
	}

	@Override
	public void onCompleteDocument(CompleteDocumentEvent event) {
		if(task instanceof Document){
			return;
		}
		
		if(task.getId().equals(event.getTaskId())){
			Map<String, Value> arguments = event.getResults();
			arguments.put("documentId", new LongValue(((HTSummary)task).getDocumentRef()));
			submitRequest(Actions.COMPLETE, arguments);
		}
	}
	
	@Override
	public void onExecTask(ExecTaskEvent event) {
		if(task instanceof Document){
			return;
		}
		
		if(task.getId().equals(event.getTaskId())){			
			//System.err.println("#####EXECUTING WF ACTION - "+event.getAction()+"; DOCUMENT :: "+documentId);
			submitRequest(event.getAction(), event.getValues());
		}
	}
	
	

	/**
	 * Duggan - 25/Jul/2013 - Had to add this
	 * since unbind is not automatically called, leaving removed/hidden
	 * instances of TaskItemPresenter still respondng to events
	 */
	@Override
	protected void onHide() {
		super.onHide();
		this.unbind();
	}

	@Override
	public void onAfterAttachmentReloaded(AfterAttachmentReloadedEvent event) {
		Long documentId = event.getDocumentId();
		if((task instanceof  Document)){
			Document doc = (Document)task;
			if(doc.getId().equals(documentId)){
				getView().showAttachmentIcon(true);
			}
			
		}else if(task instanceof HTSummary){
			HTSummary doc= (HTSummary)task;
			if(doc.getDocumentRef()==null){
				//This happens if this task was not loaded correctly.
				//this should be changed to processinstanceid - documents dont matter; processes do.
				return;
			}
			
			assert doc.getDocumentRef()!=null;
			assert documentId!=null;
			if(doc.getDocumentRef().equals(documentId)){
				getView().showAttachmentIcon(true);
			}
		}
	}

	@Override
	public void onAfterSearch(AfterSearchEvent event) {
		getView().highlight(event.getSubject(), event.getDescription());
	}
	
}
