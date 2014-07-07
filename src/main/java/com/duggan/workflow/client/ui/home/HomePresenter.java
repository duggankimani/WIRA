package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.activityfeed.ActivitiesPresenter;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent.AlertLoadHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.duggan.workflow.client.ui.profile.ProfilePresenter;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.save.form.GenericFormPresenter;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
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
ProcessingHandler, ProcessingCompletedHandler, AlertLoadHandler{

	public interface IHomeView extends TabView {
		//void bindAlerts(HashMap<TaskType, Integer> alerts);
		void refreshTabs();
		void changeTab(Tab tab, TabData tabData, String historyToken);
		void showmask(boolean b);
		void bindAlerts(HashMap<TaskType, Integer> alerts);
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

	
	@Inject
	public HomePresenter(final EventBus eventBus, final IHomeView view,
			final MyProxy proxy,
			Provider<CreateDocPresenter> docProvider,
			Provider<GenericFormPresenter> formProvider,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider,
			Provider<ActivitiesPresenter> activitiesProvider,
			Provider<ProfilePresenter> profileProvider) {
		super(eventBus, view, proxy,SLOT_SetTabContent,SLOT_RequestTabs, SLOT_ChangeTab,MainPagePresenter.CONTENT_SLOT);
		
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(AlertLoadEvent.TYPE, this);
		
		/*getView().getAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//showEditForm(MODE.CREATE);
				//showEditForm();
				
				
				getView().setDocPopupVisible();
			}
			
		});*/
		
		
		
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

}
