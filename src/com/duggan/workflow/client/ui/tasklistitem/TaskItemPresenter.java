package com.duggan.workflow.client.ui.tasklistitem;

import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent.AfterDocumentLoadHandler;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent.CompleteDocumentHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.duggan.workflow.client.ui.events.ExecTaskEvent;
import com.duggan.workflow.client.ui.events.ExecTaskEvent.ExecTaskHandler;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.ParamValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

/**
 * This class displays a Task or a Document 
 * 
 * @author duggan
 *
 */
public class TaskItemPresenter extends
		PresenterWidget<TaskItemPresenter.MyView> 
	implements DocumentSelectionHandler, AfterDocumentLoadHandler, CompleteDocumentHandler, ExecTaskHandler{

	public interface MyView extends View {
		void bind(DocSummary summaryTask);
		
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
		
	}

	DocSummary task;
	
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
	ExecuteWorkflow workflow;
	
	@Inject
	public TaskItemPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(CompleteDocumentEvent.TYPE, this);
		addRegisteredHandler(AfterDocumentLoadEvent.TYPE, this);
		addRegisteredHandler(DocumentSelectionEvent.TYPE, this);
		addRegisteredHandler(ExecTaskEvent.TYPE, this);
//		
		workflow = new ExecuteWorkflow(0l, AppContext.getUserId(), Actions.START);
		 
		getView().getFocusContainer().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(task instanceof Document){
					Document doc = (Document)task;
					fireEvent(new DocumentSelectionEvent(doc.getId(), DocMode.READWRITE));
				}else{
					fireEvent(new DocumentSelectionEvent(((HTSummary)task).getDocumentRef(), DocMode.READ));
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
						PlaceRequest request = new PlaceRequest("home").
								with("type", TaskType.APPROVALREQUESTNEW.getURL());
						
						placeManager.revealPlace(request);
						
					}
				});				
			}
		});
		
		getView().getViewLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				PlaceRequest request = new PlaceRequest(NameTokens.personalreview);
				request = request.with("taskId", task.getId()+"");				
				placeManager.revealPlace(request);
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
				completeDocument(true);
			}
		});
		
		getView().getRejectLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				completeDocument(false);
			}
		});
		
	}
	
	void completeDocument(boolean approved){
		Map<String, ParamValue> arguments = new HashMap<String, ParamValue>();	
		arguments.put("isApproved", new BooleanValue(approved));
		
		submitRequest(Actions.COMPLETE, arguments);
	}

	protected void submitRequest(Actions action){
		submitRequest(action, null);
	}
	
	protected void submitRequest(final Actions action, Map<String, ParamValue> values) {
		//String docUrl = (GWT.getModuleBaseURL()+"/search?");
		//values.put("DocumentURL", new StringValue(docUrl));
		
		workflow.setAction(action);
		workflow.setValues(values);
				
		if(task instanceof Document){
			//workflow.setTaskId(new Long((Integer)task.getId()));
		}			
		else
			workflow.setTaskId((Long)task.getId());
		
		dispatcher.execute(workflow, new TaskServiceCallback<ExecuteWorkflowResult>() {
			
			@Override
			public void processResult(ExecuteWorkflowResult result) {
				//refresh list
								
				if(action==Actions.COMPLETE){
					removeFromParent();
					fireEvent(new AfterSaveEvent());
				}else{
					//fireEvent(new ReloadEvent());
					reload((HTSummary)result.getDocument());
				}
			}
		});
	}

	protected void reload(HTSummary summary) {
		setDocSummary(summary);
		if(task instanceof Document){
			Document doc = (Document)task;
			fireEvent(new DocumentSelectionEvent(doc.getId(), DocMode.READWRITE));
		}else{
			fireEvent(new DocumentSelectionEvent(((HTSummary)task).getDocumentRef(), DocMode.READ));
		}
		
	}

	protected void removeFromParent() {
		unbind();
		this.getView().asWidget().removeFromParent();
	}

	public void setDocSummary(DocSummary summaryTask) {
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
		if(task instanceof  Document){
			Document doc = (Document)task;
			if(doc.getId()!=documentId){
				getView().setSelected(false);
			}else{
				getView().setSelected(true);
			}
		}else if(task instanceof HTSummary){
			Long docRef= ((HTSummary) task).getDocumentRef();
			
			if(docRef==null || docRef!=documentId){
				getView().setSelected(false);
			}else{
				getView().setSelected(true);
			}
			
		}else{
			getView().setSelected(false);
		}
	}

	@Override
	public void onAfterDocumentLoad(AfterDocumentLoadEvent event) {
		if(task instanceof Document){
			return;
		}
		
		HTSummary summary = (HTSummary)task;
		
		if(summary.getDocumentRef()!=null && summary.getDocumentRef()==event.getDocumentId()){
			event.setValidActions(summary.getStatus().getValidActions());
		}
	}

	@Override
	public void onCompleteDocument(CompleteDocumentEvent event) {
		if(task instanceof Document){
			return;
		}
		
		HTSummary summary = (HTSummary)task;
		
		Long ref= summary.getDocumentRef();
		Long documentId = event.getDocumentId();
		
		if(ref==documentId){			
			completeDocument(event.IsApproved());
		}
	}
	
	@Override
	public void onExecTask(ExecTaskEvent event) {
		if(task instanceof Document){
			return;
		}
		
		HTSummary summary = (HTSummary)task;
		
		Long ref= summary.getDocumentRef();
		Long documentId = event.getDocumentId();
		
		if(ref==documentId){			
			submitRequest(event.getAction());
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
	
}
