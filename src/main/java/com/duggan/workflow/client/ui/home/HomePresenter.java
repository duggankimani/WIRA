package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.client.model.MODE;
import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.addDoc.DocumentPopupPresenter;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent.AlertLoadHandler;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.duggan.workflow.client.ui.events.CreateDocumentEvent;
import com.duggan.workflow.client.ui.events.CreateDocumentEvent.CreateDocumentHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.save.form.GenericFormPresenter;
import com.duggan.workflow.shared.model.DocumentType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.ChangeTabHandler;
import com.gwtplatform.mvp.client.RequestTabsHandler;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabContainerPresenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabView;
import com.gwtplatform.mvp.client.annotations.ChangeTab;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.RequestTabs;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class HomePresenter extends TabContainerPresenter<HomePresenter.IHomeView, HomePresenter.MyProxy> implements
ProcessingHandler, ProcessingCompletedHandler, AlertLoadHandler,CreateDocumentHandler{

	public interface IHomeView extends TabView {
		//void bindAlerts(HashMap<TaskType, Integer> alerts);
		void refreshTabs();
		void changeTab(Tab tab, TabData tabData, String historyToken);
		void showmask(boolean b);
		void bindAlerts(HashMap<TaskType, Integer> alerts);
		HasClickHandlers getAddButton();
		void showDocsList();
	}
	
	@ProxyStandard
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
    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_SetTabContent = new Type<RevealContentHandler<?>>();


	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCPOPUP_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject DocumentPopupPresenter docPopup;
	private IndirectProvider<CreateDocPresenter> createDocProvider;
	private IndirectProvider<GenericFormPresenter> genericFormProvider;
	
	@Inject
	public HomePresenter(final EventBus eventBus, final IHomeView view,
			final MyProxy proxy,
			Provider<CreateDocPresenter> docProvider,
			Provider<GenericFormPresenter> formProvider) {
		super(eventBus, view, proxy,SLOT_SetTabContent,SLOT_RequestTabs, SLOT_ChangeTab,MainPagePresenter.CONTENT_SLOT);
		createDocProvider = new StandardProvider<CreateDocPresenter>(docProvider);
		genericFormProvider = new StandardProvider<GenericFormPresenter>(formProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(AlertLoadEvent.TYPE, this);
		addRegisteredHandler(CreateDocumentEvent.TYPE, this);
		
//		getView().getAddButton().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				//showEditForm(MODE.CREATE);
//				//showEditForm();
//				
//				getView().showDocsList();
//			}
//			
//		});
		
		
	}
	
	
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(DOCPOPUP_SLOT, docPopup);
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
		//event.getAlerts();
		getView().bindAlerts(event.getAlerts());		
	}
	
	@Override
	public void onCreateDocument(CreateDocumentEvent event) {
		DocumentType type = event.getDocType();	
		
		if(type.getFormId()!=null){
			showEditForm(type);
		}else{
			showEditForm(MODE.CREATE);
		}
	}
	
	protected void showEditForm(final MODE mode) {
		createDocProvider.get(new ServiceCallback<CreateDocPresenter>() {
			@Override
			public void processResult(CreateDocPresenter result) {
//				if(mode.equals(MODE.EDIT) && selectedDocumentId!=null){
//					result.setDocumentId(selectedDocumentId);
//				}
					
				addToPopupSlot(result, false);
			}
		});
	}
	
	protected void showEditForm(final DocumentType type){
		genericFormProvider.get(new ServiceCallback<GenericFormPresenter>() {
			@Override
			public void processResult(GenericFormPresenter result) {
				result.setDocumentType(type);
				addToPopupSlot(result, false);
			}
		});
	}


}
