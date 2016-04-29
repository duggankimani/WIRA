package com.duggan.workflow.client.ui;


import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.events.AdminPageLoadEvent;
import com.duggan.workflow.client.ui.events.ClientDisconnectionEvent;
import com.duggan.workflow.client.ui.events.ClientDisconnectionEvent.ClientDisconnectionHandler;
import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ErrorEvent.ErrorHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.duggan.workflow.client.ui.events.WorkflowProcessEvent;
import com.duggan.workflow.client.ui.events.WorkflowProcessEvent.WorkflowProcessHandler;
import com.duggan.workflow.client.ui.header.HeaderPresenter;
import com.duggan.workflow.client.ui.popup.ModalPopup;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent.ShowAttachmentHandler;
import com.duggan.workflow.client.ui.upload.href.IFrameDataPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.IsSlot;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class MainPagePresenter extends
		Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> 
implements ErrorHandler, ProcessingCompletedHandler, 
ProcessingHandler ,WorkflowProcessHandler, ShowAttachmentHandler, ClientDisconnectionHandler{

	public interface MyView extends View {

		void showProcessing(boolean processing,String message);
		void setAlertVisible(String subject, String action, String url);
		void showDisconnectionMessage(String message);
		void clearDisconnectionMsg();
		ModalPopup getModalPopup();
		ModalPopup getModalPopup(boolean reInstantiate);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<MainPagePresenter> {
	}

	public static final PermanentSlot<HeaderPresenter> HEADER_content = new PermanentSlot<HeaderPresenter>();
	
	public static final NestedSlot CONTENT_SLOT = new NestedSlot();

	@Inject HeaderPresenter headerPresenter;
		
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;

	@Inject IFrameDataPresenter presenter;
	
	@Inject
	public MainPagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy,RevealType.Root);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ErrorEvent.TYPE, this);
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(WorkflowProcessEvent.TYPE, this);
		addRegisteredHandler(ShowAttachmentEvent.TYPE, this);
		addRegisteredHandler(ClientDisconnectionEvent.TYPE, this);
	}
	
	
	
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(HEADER_content, headerPresenter);	
		getView().clearDisconnectionMsg();
		//System.err.println("Main Page - Reset called......");
	}
	
	@Override
	public void onError(final ErrorEvent event) {
		//addToPopupSlot(null);
		
		HTMLPanel panel = new HTMLPanel(event.getMessage());
		Anchor anchor = new Anchor("View");
		anchor.setHref("#error;errorid="+event.getId());
		panel.add(anchor);
		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().getModalPopup().hide();
			}
		});
		
		AppManager.showPopUp("Error", panel, new OnOptionSelected() {
			@Override
			public void onSelect(String name) {
			}
		}, "Ok");
	}

	@Override
	public <T extends PresenterWidget<?>> void setInSlot(IsSlot<T> slot, T child) {
		super.setInSlot(slot, child);
//		Window.alert(">> Called [1]");
		if(slot==CONTENT_SLOT){
			if(child!=null && child instanceof AdminHomePresenter){
				fireEvent(new AdminPageLoadEvent(true));
			}else{
				fireEvent(new AdminPageLoadEvent(false));
			}
		}
	}
	
	@Override
	public <T extends PresenterWidget<?>> void setInSlot(IsSlot<T> slot,
			T child, boolean performReset) {
//		Window.alert(">> Called [2]");
		super.setInSlot(slot, child, performReset);
	}

	@Override
	public void onProcessing(ProcessingEvent event) {
		getView().showProcessing(true,event.getMessage());
	}

	@Override
	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showProcessing(false, null);
	}
	
	@Override
	public void onWorkflowProcess(WorkflowProcessEvent event) {
		Doc summary = event.getDocument();
		String url = "";
		url = "#search;docRefId="+summary.getRefId();
		
//		if(summary instanceof Document){
//			//url = "#search;did="+summary.getId();
//			url = "#search;did="+summary.getId();
//		}else{
//			long processInstanceId = ((HTSummary)summary).getProcessInstanceId();
//			url = "#search;pid="+processInstanceId;
//			
//		}
		
		getView().setAlertVisible(event.getSubject(), event.getAction(),url);
	}
	
	@Override
	public void onShowAttachment(ShowAttachmentEvent event) {
		REPORTVIEWIMPL reportView = event.getViewImplementation();
		if(reportView==null){
			reportView = AppContext.getReportViewImpl()==null? 
					REPORTVIEWIMPL.getDefaultImplementation(): AppContext.getReportViewImpl();
		}
		
		switch (reportView) {
		case IFRAME:
			presenter.setInfo(event.getUri(), event.getTitle());
			addToPopupSlot(presenter, true);
			break;
		case NEW_TAB:
			Window.open(event.getUri(), "_blank", null);
			break;
		case GOOGLE_DOCS:
			
			String docsUri = "http://docs.google.com/gview?url=";
			String uri = docsUri+ event.getUri();
			uri = uri+"&embedded=true";
			
			presenter.setInfo(uri, event.getTitle());
			addToPopupSlot(presenter, true);
			break;	
		default:
			Window.open(event.getUri(), "_blank", null);
			break;
		}
		
	}
	
	@Override
	protected void onUnbind() {
		super.onUnbind();
		headerPresenter.unbind();
	}

	@Override
	public void onClientDisconnection(ClientDisconnectionEvent event) {
		getView().showDisconnectionMessage(event.getMessage());
	}

}
