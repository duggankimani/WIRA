package com.duggan.workflow.client.ui.tasklistitem;

import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent.CompleteDocumentHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent.AfterDocumentLoadHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.duggan.workflow.client.ui.events.ReloadEvent;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.CurrentUser;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.ParamValue;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * This class displays a Task or a Document 
 * 
 * @author duggan
 *
 */
public class TaskItemPresenter extends
		PresenterWidget<TaskItemPresenter.MyView> 
	implements DocumentSelectionHandler, AfterDocumentLoadHandler, CompleteDocumentHandler{

	public interface MyView extends View {

		void setRowNo(int row);

		void bind(DocSummary summaryTask);
		
		FocusPanel getContainer();
		
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
		
		void setSelected(boolean selected);
		
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
		
		workflow = new ExecuteWorkflow(0l, AppContext.getUserId(), Actions.START);
		 
		this.addHandler(DocumentSelectionEvent.TYPE, this);
		
		getView().getContainer().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//getView().setSelected(true);				
				if(task instanceof Document){
					//check status too
					//local tasks
					Document doc = (Document)task;
					fireEvent(new DocumentSelectionEvent(doc.getId(), DocMode.READWRITE));
				}else{
					fireEvent(new DocumentSelectionEvent(((HTSummary)task).getDocumentRef(), DocMode.READ));
				}
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
								with("type", TaskType.APPROVALREQUESTNEW.getDisplayName());
						
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
	
	protected void submitRequest(Actions action, Map<String, ParamValue> values) {
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
				fireEvent(new ReloadEvent());
			}
		});
	}

	public void setRowNo(int row) {
		getView().setRowNo(row);
	}

	public void setDocSummary(DocSummary summaryTask) {
		this.task = summaryTask;
		
		if(summaryTask!=null){
			getView().bind(summaryTask);
			
		}
	}
	
	@Override
	public void onDocumentSelection(DocumentSelectionEvent event) {
		Integer documentId = event.getDocumentId();
		if(task instanceof  Document){
			Document doc = (Document)task;
			if(doc.getId()!=documentId){
				getView().setSelected(false);
			}else{
				getView().setSelected(true);
			}
		}else if(task instanceof HTSummary){
			Integer docRef= ((HTSummary) task).getDocumentRef();
			
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
		
		//System.err.println(event.getDocumentId()+" ");
		
		if(summary.getDocumentRef()!=null && (summary.getDocumentRef()==event.getDocumentId()) 
			&& summary.getDocumentRef()!=0){
			
			completeDocument(event.IsApproved());
		}
	}
}
