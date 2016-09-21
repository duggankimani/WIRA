package com.duggan.workflow.client.ui.applicationscentral;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent.AfterDocumentLoadHandler;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
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
implements AfterDocumentLoadHandler
{
    interface MyView extends View  {

		void setContext(Doc doc);
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

    @Inject
    ApplicationsCentralPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy,
            Provider<GenericDocumentPresenter> docViewProvider) {
        super(eventBus, view, proxy, RevealType.RootLayout);
        docViewFactory = new StandardProvider<GenericDocumentPresenter>(
				docViewProvider);
    }
    
    @Override
    protected void onBind() {
    	super.onBind();
    	addRegisteredHandler(AfterDocumentLoadEvent.TYPE, this);
    }
    
    @Override
    public void prepareFromRequest(PlaceRequest request) {
    	super.prepareFromRequest(request);
    	Window.setTitle("Applications Central");
    	
    	String processRefId = request.getParameter("processRefId", null);
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
				result.setDocId(docRefId, taskId, false);
				
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
	public void onAfterDocumentLoad(AfterDocumentLoadEvent event) {
		Doc doc = event.getDoc();
		getView().setContext(doc);
	}
}