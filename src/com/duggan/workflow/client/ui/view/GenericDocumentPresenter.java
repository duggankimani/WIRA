package com.duggan.workflow.client.ui.view;

import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.model.MODE;
import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;

public class GenericDocumentPresenter extends
		PresenterWidget<GenericDocumentPresenter.MyView>{

	public interface MyView extends View {
		public void setValues(Date created, DocType type, String subject,
				Date docDate, String value, String partner, String description, Integer priority,DocStatus status);
		
		HasClickHandlers getForward();
		
		void showForward(boolean show);
		
		void setValidTaskActions(List<Actions> actions);
		
		public HasClickHandlers getApproveButton();
		
		public HasClickHandlers getRejectButton();

		public void show(boolean IsShowapprovalLink, boolean IsShowRejectLink);
		
		HasClickHandlers getSimulationBtn();
		HasClickHandlers getEditButton();
		void showEdit(boolean displayed);
	}

	Long documentId;
	
	Document doc;
	
	@Inject DispatchAsync requestHelper;
	
	@Inject PlaceManager placeManager;
	
	private IndirectProvider<CreateDocPresenter> createDocProvider;
	
	@Inject
	public GenericDocumentPresenter(final EventBus eventBus, final MyView view, Provider<CreateDocPresenter> docProvider) {
		super(eventBus, view);		
		createDocProvider = new StandardProvider<CreateDocPresenter>(docProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();		
		
		getView().getForward().addClickHandler(new ClickHandler() {
			
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
		
		getView().getApproveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().show(false,false);
				fireEvent(new CompleteDocumentEvent(documentId, true));				
			}
		});
		
		getView().getRejectButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().show(false,false);
				fireEvent(new CompleteDocumentEvent(documentId,false));
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
		if(documentId != null){
			requestHelper.execute(new GetDocumentRequest(documentId), 
					new TaskServiceCallback<GetDocumentResult>() {
				
				public void processResult(GetDocumentResult result) {
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
					
					getView().setValues(created,
							docType, subject, docDate,  value, partner, description, priority,status);
					
					if(status==DocStatus.DRAFTED){
						getView().showEdit(true);
					}else{
						clear();
					}
					
					AfterDocumentLoadEvent e = new AfterDocumentLoadEvent(documentId);
					fireEvent(e);
					if(e.getValidActions()!=null){
						getView().setValidTaskActions(e.getValidActions());
					}
					
				}
			});
		}
	}

	public void setDocumentId(Long selectedValue) {
		this.documentId=selectedValue;
	}
	
}
