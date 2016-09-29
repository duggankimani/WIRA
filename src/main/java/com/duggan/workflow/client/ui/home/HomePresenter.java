package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.ApplicationPresenter;
import com.duggan.workflow.client.ui.addDoc.DocTypesPresenter;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent.AlertLoadHandler;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.duggan.workflow.client.ui.events.CreateDocumentEvent;
import com.duggan.workflow.client.ui.events.CreateDocumentEvent.CreateDocumentHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ChangeTabHandler;
import com.gwtplatform.mvp.client.RequestTabsHandler;
import com.gwtplatform.mvp.client.TabContainerPresenter;
import com.gwtplatform.mvp.client.TabView;
import com.gwtplatform.mvp.client.annotations.ChangeTab;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.RequestTabs;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class HomePresenter extends TabContainerPresenter<HomePresenter.IHomeView, HomePresenter.MyProxy> implements
ProcessingHandler, ProcessingCompletedHandler, AlertLoadHandler,CreateDocumentHandler, ContextLoadedHandler{

	public interface IHomeView extends TabView {
		//void bindAlerts(HashMap<TaskType, Integer> alerts);
		void refreshTabs();
//		void changeTab(Tab tab, TabData tabData, String historyToken);
		void showmask(boolean b);
		void bindAlerts(HashMap<TaskType, Integer> alerts, String processRefId);
		HasClickHandlers getAddButton();
		void showDocsList();
		void load();
		void closeDocTypePopup();
		void clearAnchors();
	}
	
	@ProxyCodeSplit
	public interface MyProxy extends Proxy<HomePresenter> {
	}

	/**
     * This will be the event sent to our "unknown" child presenters, in order for them to register their tabs.
     */
    @RequestTabs
    public static final Type<RequestTabsHandler> SLOT_RequestTabs = new Type<RequestTabsHandler>();

    /**
     * Fired by child proxie's when their tab content is changed.
     */
    @ChangeTab
    public static final Type<ChangeTabHandler> SLOT_ChangeTab = new Type<ChangeTabHandler>();

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    public static final NestedSlot SLOT_SetTabContent = new NestedSlot();
    
    public static final PermanentSlot<DocTypesPresenter> DOCTREE_SLOT = new PermanentSlot<DocTypesPresenter>();
		
	@Inject DocTypesPresenter docPopup;
	
	@Inject DispatchAsync requestHelper;
	@Inject PlaceManager placeManager;
	
	@Inject
	public HomePresenter(final EventBus eventBus, final IHomeView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy,SLOT_SetTabContent,SLOT_RequestTabs, 
				SLOT_ChangeTab,ApplicationPresenter.CONTENT_SLOT);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().load();
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(AlertLoadEvent.TYPE, this);
		addRegisteredHandler(CreateDocumentEvent.TYPE, this);
		addRegisteredHandler(ContextLoadedEvent.getType(), this);
		setInSlot(DOCTREE_SLOT, docPopup);
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
	}
	
	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showmask(false);
	}

	@Override
	public void onProcessing(ProcessingEvent event) {		
		getView().showmask(true);
	}
	
	@ProxyEvent
	public void onContextLoaded(ContextLoadedEvent event){
		getView().refreshTabs();
	}
	
	@Override
	public void onAlertLoad(AlertLoadEvent event) {
		getView().bindAlerts(event.getAlerts(),event.getProcessRefId());		
	}
	
	@Override
	public void onCreateDocument(CreateDocumentEvent event) {
		getView().closeDocTypePopup();
		Document doc = new Document();
		doc.setType(event.getDocType());
		
		final String processRefId = event.getProcessRefId();
		
		CreateDocumentRequest request = null;
		
		if(event.getProcessRefId()!=null){
			request = new CreateDocumentRequest(event.getProcessRefId());
		}else{
			request = new CreateDocumentRequest(doc);
		}
		
		requestHelper.execute(request, new TaskServiceCallback<CreateDocumentResult>() {
			@Override
			public void processResult(CreateDocumentResult aResponse) {
				PlaceRequest.Builder request = new PlaceRequest.Builder().nameToken(NameTokens.search)
						.with("docRefId", aResponse.getDocument().getRefId())
						.with("mode", "edit");
				if(processRefId!=null){
					request.with("processRefId",processRefId);
				}
				
				placeManager.revealPlace(request.build());
			}
		});
	}
	
}
