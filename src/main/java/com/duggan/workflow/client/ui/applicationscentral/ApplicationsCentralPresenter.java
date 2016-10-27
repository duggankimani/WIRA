package com.duggan.workflow.client.ui.applicationscentral;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.ApplicationPresenter;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.document.VIEWS;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent.AfterDocumentLoadHandler;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.shared.event.FormLoadEvent;
import com.duggan.workflow.shared.event.ForwardDocumentEvent;
import com.duggan.workflow.shared.event.SaveDocumentEvent;
import com.duggan.workflow.shared.event.ShowViewEvent;
import com.duggan.workflow.shared.event.FormLoadEvent.FormLoadHandler;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.presenter.slots.SingleSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.client.service.ServiceCallback;
public class ApplicationsCentralPresenter extends Presenter<ApplicationsCentralPresenter.MyView, ApplicationsCentralPresenter.MyProxy>  
implements FormLoadHandler
{
    interface MyView extends View  {

		void setContext(Form form, Doc doc);

		HasClickHandlers getForwardForApproval();

		HasClickHandlers getSaveButton();

		HasClickHandlers getShowProcessMap();

		HasClickHandlers getEditLink();

		HasClickHandlers getShowAttachmentsLink();

		HasClickHandlers getSaveDocLink();

		HasClickHandlers getViewAuditLog();

		HasClickHandlers getViewDocButton();
		
    }

    public static final SingleSlot<GenericDocumentPresenter> DOCUMENT_SLOT = new SingleSlot<GenericDocumentPresenter>();
    
    private IndirectProvider<GenericDocumentPresenter> docViewFactory;
    
    @UseGatekeeper(LoginGateKeeper.class)
    @NameToken({NameTokens.applications, NameTokens.applicationsNew, NameTokens.applicationsBase})
    @ProxyCodeSplit
    interface MyProxy extends ProxyPlace<ApplicationsCentralPresenter> {
    }
    
    @Inject PlaceManager placeManager;
    @Inject DispatchAsync requestHelper;

	private String processRefId;

	private Doc doc;

    @Inject
    ApplicationsCentralPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy,
            Provider<GenericDocumentPresenter> docViewProvider) {
        super(eventBus, view, proxy, ApplicationPresenter.CONTENT_SLOT);
        docViewFactory = new StandardProvider<GenericDocumentPresenter>(
				docViewProvider);
    }
    
    @Override
    protected void onBind() {
    	super.onBind();
    	addRegisteredHandler(FormLoadEvent.getType(), this);
    	
    	getView().getForwardForApproval().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ForwardDocumentEvent(doc));
			}
		});

    	getView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new SaveDocumentEvent(doc));
			}
		});

//    	getView().getDeleteButton().addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				delete((Document) doc);
//			}
//		});

    	getView().getShowProcessMap().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.PROCESSTREE);
			}
		});

		getView().getEditLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.FORM);
			}
		});

		getView().getShowAttachmentsLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.ATTACHMENTS);
			}
		});

		getView().getViewAuditLog().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.AUDITLOG);
			}
		});
		
		getView().getViewDocButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.FORM);
			}
		});
		

    }
    
    protected void changeView(VIEWS view) {
    	fireEvent(new ShowViewEvent(view));
	}

	@Override
    public void prepareFromRequest(PlaceRequest request) {
    	super.prepareFromRequest(request);
    	Window.setTitle("Applications Central");
    	
    	processRefId = request.getParameter("processRefId", null);
    	String docRefId = request.getParameter("docRefId", null);
    	
    	if(docRefId!=null){
    		loadData(docRefId, null);
    	}else if(processRefId!=null){
    		createDocument(processRefId);
    	}
    	
    }

	private void loadData(final String docRefId, final Long taskId) {
		if (docRefId == null && taskId == null) {
			setInSlot(DOCUMENT_SLOT, null);
			return;
		}
		
		docViewFactory.get(new ServiceCallback<GenericDocumentPresenter>() {
			@Override
			public void processResult(GenericDocumentPresenter result) {
				result.setDocId(processRefId,docRefId, taskId, false);
				
//				result.setGlobalFormMode(mode);
//				if (currentTaskType == TaskType.UNASSIGNED) {
//					result.setUnAssignedList(true);
//				}

				setInSlot(DOCUMENT_SLOT, result);
			}
		});

	}
    
	private void createDocument(final String processRefId) {
		CreateDocumentRequest request = new CreateDocumentRequest(processRefId);
		
		requestHelper.execute(request, new TaskServiceCallback<CreateDocumentResult>() {
			@Override
			public void processResult(CreateDocumentResult aResponse) {
				PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.applications)
						.with("processRefId",processRefId)
						.with("docRefId", aResponse.getDocument().getRefId())
						.with("mode", "edit")
						.build();
				placeManager.revealPlace(request);
			}
		});
	}
	
	@Override
	public void onFormLoad(FormLoadEvent event) {
		getView().setContext(event.getForm(),event.getDoc());
	}
}