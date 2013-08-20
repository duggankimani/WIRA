package com.duggan.workflow.client.ui.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.model.MODE;
import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.comments.CommentPresenter;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent;
import com.duggan.workflow.client.ui.events.ExecTaskEvent;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.NodeDetail;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.GetCommentsRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class GenericDocumentPresenter extends
		PresenterWidget<GenericDocumentPresenter.MyView>{

	public interface MyView extends View {
		void setValues(HTUser createdBy, Date created, DocType type, String subject,
				Date docDate, String value, String partner, String description, Integer priority,DocStatus status);
		
		void showForward(boolean show);
		void setValidTaskActions(List<Actions> actions);
		void show(boolean IsShowapprovalLink, boolean IsShowRejectLink);
		void showEdit(boolean displayed);
		void setStates(List<NodeDetail> states);
		HasClickHandlers getSimulationBtn();
		HasClickHandlers getEditButton();
		HasClickHandlers getForwardForApproval();		
		HasClickHandlers getClaimLink();
		HasClickHandlers getStartLink();
		HasClickHandlers getSuspendLink();
		HasClickHandlers getResumeLink();
		HasClickHandlers getCompleteLink();
		HasClickHandlers getDelegateLink();
		HasClickHandlers getRevokeLink();
		HasClickHandlers getStopLink();
		HasClickHandlers getApproveLink();
		HasClickHandlers getRejectLink();
	}

	Long documentId;
	
	Document doc;
	
	@Inject DispatchAsync requestHelper;
	
	@Inject PlaceManager placeManager;
	
	private IndirectProvider<CreateDocPresenter> createDocProvider;
	private IndirectProvider<CommentPresenter> commentPresenterFactory;
	
	public static final Object ACTIVITY_SLOT = new Object();
	
	@Inject
	public GenericDocumentPresenter(final EventBus eventBus, final MyView view,
			Provider<CreateDocPresenter> docProvider, Provider<CommentPresenter> commentProvider) {
		super(eventBus, view);		
		createDocProvider = new StandardProvider<CreateDocPresenter>(docProvider);
		commentPresenterFactory = new StandardProvider<CommentPresenter>(commentProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();		
		
		getView().getForwardForApproval().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				requestHelper.execute(new ApprovalRequest(AppContext.getUserId(), doc), new TaskServiceCallback<ApprovalRequestResult>(){
					@Override
					public void processResult(ApprovalRequestResult result) {
						GenericDocumentPresenter.this.getView().asWidget().removeFromParent();
						//clear selected document
						fireEvent(new AfterSaveEvent());
					}
				});
				
			}
		});
		
		getView().getEditButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(doc.getStatus()==DocStatus.DRAFTED)
					showEditForm(MODE.EDIT);
				}
		});
		
		//testing code
		getView().getSimulationBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document doc = new Document();
				doc.setCreated(new Date());
				doc.setDateDue(new Date());
				doc.setSubject("CNT/B&C/01/2013");
				doc.setDescription("Contract for the constrution of Hall6");
				doc.setPartner("B&C Contactors");
				doc.setValue("5.5Mil");
				doc.setType(DocType.CONTRACT);
				
				requestHelper.execute(new ApprovalRequest(AppContext.getUserId(), doc), new TaskServiceCallback<ApprovalRequestResult>(){
					@Override
					public void processResult(ApprovalRequestResult result) {						
						PlaceRequest request = new PlaceRequest("home").
								with("type", TaskType.APPROVALREQUESTNEW.getURL());
						
						placeManager.revealPlace(request);
						
					}
				});
			}
		});

		getView().getClaimLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.CLAIM);
				fireEvent(new ExecTaskEvent(documentId, Actions.CLAIM));
			}
		});
		
		getView().getStartLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.START);
				fireEvent(new ExecTaskEvent(documentId, Actions.START));
			}
		});
		
		getView().getSuspendLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.SUSPEND);
				fireEvent(new ExecTaskEvent(documentId, Actions.SUSPEND));
			}
		});
		
		getView().getResumeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.RESUME);
				fireEvent(new ExecTaskEvent(documentId, Actions.RESUME));
			}
		});
		
		getView().getCompleteLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.COMPLETE);
				fireEvent(new ExecTaskEvent(documentId, Actions.COMPLETE));
			}
		});
		
		getView().getDelegateLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.DELEGATE);
				fireEvent(new ExecTaskEvent(documentId, Actions.DELEGATE));
			}
		});
		
		getView().getRevokeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//submitRequest(Actions.REVOKE);
				fireEvent(new ExecTaskEvent(documentId, Actions.REVOKE));
			}
		});
		
		getView().getStopLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ExecTaskEvent(documentId, Actions.STOP));				
			}
		});
		
		getView().getApproveLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new CompleteDocumentEvent(documentId, true));
			}
		});
		
		getView().getRejectLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new CompleteDocumentEvent(documentId, false));
			}
		});
		
	}

	protected void showEditForm(final MODE mode) {
		createDocProvider.get(new ServiceCallback<CreateDocPresenter>() {
			@Override
			public void processResult(CreateDocPresenter result) {
				if(mode.equals(MODE.EDIT)){
					result.setDocumentId(documentId);
				}
				
				addToPopupSlot(result, true);				
			}
		});
	}

	private void clear() {
		getView().showEdit(false);
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetDocumentRequest(documentId));
		requests.addRequest(new GetCommentsRequest(documentId));
		
		if(documentId != null){
			requestHelper.execute(requests, 
					new TaskServiceCallback<MultiRequestActionResult>() {
				
				public void processResult(MultiRequestActionResult results) {					
					
					GetDocumentResult result = (GetDocumentResult)results.get(0);
					bindDocumentResult(result);
					
					GetCommentsResponse commentsResult = (GetCommentsResponse)results.get(1);
					bindCommentsResult(commentsResult);
					
					List<NodeDetail> details = new ArrayList<NodeDetail>();
					details.add(new NodeDetail("Created", true,false));
					details.add(new NodeDetail("HOD Development", false,false));
					details.add(new NodeDetail("Finance", false,false));
					details.add(new NodeDetail("Gatheru", false,false));
					details.add(new NodeDetail("Complete", false,true));
					setProcessState(details);
					
				}
			});
		}
	}
	
	protected void bindCommentsResult(GetCommentsResponse commentsResult) {
		setInSlot(ACTIVITY_SLOT, null);
		List<Comment> comments = commentsResult.getComments();
		for(final Comment comment: comments){
			commentPresenterFactory.get(new ServiceCallback<CommentPresenter>() {
				@Override
				public void processResult(CommentPresenter result) {
					result.setComment(comment);
					addToSlot(ACTIVITY_SLOT,result);
				}
			});
		}
	}

	protected void bindDocumentResult(GetDocumentResult result) {

		Document document = result.getDocument();
		doc = document;
		
		Date created = document.getCreated();
		DocType docType = document.getType();	
		String subject = document.getSubject();						
		Date docDate = document.getDocumentDate();					
		String partner = document.getPartner();
		String value= document.getValue();			
		String description = document.getDescription();
		Integer priority = document.getPriority();									
		DocStatus status = document.getStatus();
		
		getView().setValues(doc.getOwner(),created,
				docType, subject, docDate,  value, partner, description, priority,status);
		
		if(status==DocStatus.DRAFTED){
			getView().showEdit(true);
		}else{
			clear();
		}
		
		//get document actions - if any
		AfterDocumentLoadEvent e = new AfterDocumentLoadEvent(documentId);
		fireEvent(e);		
		if(e.getValidActions()!=null){
			getView().setValidTaskActions(e.getValidActions());
		}		
	}

	public void setProcessState(List<NodeDetail> states){
		getView().setStates(states);
	}

	public void setDocumentId(Long selectedValue) {
		this.documentId=selectedValue;
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		this.unbind();
	}
	
}
