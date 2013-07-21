package com.duggan.workflow.client.ui.save;

import java.util.Date;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Priority;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class CreateDocPresenter extends
		PresenterWidget<CreateDocPresenter.ICreateDocView> {

	public interface ICreateDocView extends PopupView {
		HasClickHandlers getSave();
		HasClickHandlers getCancel();
		HasClickHandlers getForward();
		Document getDocument();
		boolean isValid();
		void setValues(DocType docType, String subject, Date docDate,
				String partner, String value, String description,
				Priority priority);
	}

	@Inject DispatchAsync requestHelper;
	
	private Integer Id;
	
	@Inject PlaceManager placeManager;
	
	@Inject
	public CreateDocPresenter(final EventBus eventBus, final ICreateDocView view) {
		super(eventBus, view);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		if(Id != null){
			requestHelper.execute(new GetDocumentRequest(Id), 
					new TaskServiceCallback<GetDocumentResult>() {
				
				public void processResult(GetDocumentResult result) {
					Document document = result.getDocument();
					
					DocType docType = document.getType();	
					String subject = document.getSubject();						
					Date docDate = document.getDocumentDate();					
					String partner = document.getPartner();
					String value= document.getValue();			
					String description = document.getDescription();
					Integer priority = document.getPriority();									
										
					getView().setValues(docType, subject, docDate, partner, value, description, 
							Priority.get(priority));
				}
			});
		}
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getCancel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().hide();
			}
		});
		
		getView().getForward().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().hide();
			}
		});
		
		getView().getSave().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document document = getView().getDocument();	
				document.setStatus(DocStatus.DRAFTED);
				document.setId(Id);
				
				//document.setDescription(null);
				if(getView().isValid()){
					requestHelper.execute(new CreateDocumentRequest(document), 
							new TaskServiceCallback<CreateDocumentResult>() {
								@Override
								public void processResult(
										CreateDocumentResult result) {
									
									Document saved = result.getDocument();
									assert saved.getId()!=null;
									//fireEvent(new AfterSaveEvent());
									PlaceRequest request = new PlaceRequest("home").
											with("type", TaskType.DRAFT.getDisplayName());
									
									//loadTasks(TaskType.APPROVALREQUESTNEW);
									placeManager.revealPlace(request);
									
									getView().hide();
								}
							});				
				}
			}
		});
	}

	public void setDocumentId(Integer selectedValue) {
		this.Id = selectedValue;
	}
}
