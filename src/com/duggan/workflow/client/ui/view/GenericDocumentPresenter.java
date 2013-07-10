package com.duggan.workflow.client.ui.view;

import java.util.Date;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
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
	}

	Integer documentId;
	
	Document doc;
	
	@Inject DispatchAsync requestHelper;
	
	@Inject
	public GenericDocumentPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();		
		
		getView().getForward().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				requestHelper.execute(new ApprovalRequest("calcacuervo", doc), new TaskServiceCallback<ApprovalRequestResult>(){
					@Override
					public void processResult(ApprovalRequestResult result) {
					}
				});
				
			}
		});
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
				}
			});
		}
	}

	public void setDocumentId(Integer selectedValue) {
		this.documentId=selectedValue;
	}
	
}
