package com.duggan.workflow.client.ui;


import com.duggan.workflow.client.event.ShowMessageEvent;
import com.duggan.workflow.client.event.ShowMessageEvent.ShowMessageHandler;
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
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.IsSlot;
import com.gwtplatform.mvp.client.presenter.slots.LegacySlotConvertor;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.LockInteractionEvent;
import com.gwtplatform.mvp.client.proxy.LockInteractionHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.wira.commons.shared.models.REPORTVIEWIMPL;

public class ApplicationPresenter extends
		Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> 
implements ErrorHandler, ProcessingCompletedHandler, 
ProcessingHandler ,WorkflowProcessHandler, ShowAttachmentHandler, 
ClientDisconnectionHandler, ShowMessageHandler, LockInteractionHandler{

	public interface MyView extends View {

		void showProcessing(boolean processing,String message);
		void setAlertVisible(String subject, String action, String url);
		void showDisconnectionMessage(String message);
		void clearDisconnectionMsg();
		ModalPopup getModalPopup();
		ModalPopup getModalPopup(boolean reInstantiate);
		void setAlertVisible(AlertType alertType, String message,boolean showDefaultHeading);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ApplicationPresenter> {
	}

	public static final PermanentSlot<HeaderPresenter> HEADER_content = new PermanentSlot<HeaderPresenter>();
	
	public static final NestedSlot CONTENT_SLOT = new NestedSlot();

	@Inject HeaderPresenter headerPresenter;
		
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;

	@Inject IFrameDataPresenter presenter;
	
	@Inject
	public ApplicationPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy,RevealType.Root);
	}
	
	@Override
	protected void revealInParent() {
		RootPanel.get("loading").getElement().getStyle().setDisplay(Display.NONE);
		super.revealInParent();
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
		addRegisteredHandler(ShowMessageEvent.getType(), this);
		addRegisteredHandler(LockInteractionEvent.getType(), this);
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
		
		HTMLPanel panel = new HTMLPanel(event.getMessage()+"&nbsp;&nbsp;");
		Anchor anchor = new Anchor("View Detail");
		anchor.setHref("#/error?errorid="+event.getId());
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
		if(slot==CONTENT_SLOT){
			if(child!=null && child instanceof AdminHomePresenter){
				fireEvent(new AdminPageLoadEvent(true));
			}else{
				fireEvent(new AdminPageLoadEvent(false));
			}
		}
	}
	
	@Override
	public void setInSlot(Object slot, PresenterWidget<?> content) {
		setInSlot(LegacySlotConvertor.convert(slot), content);
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
		url = "#/search/"+summary.getRefId();
		
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
		
//		IFrameElement iframe = Document.get().createIFrameElement();
//		iframe.setAttribute("width", "100%");
//		iframe.setAttribute("height", "100%");
//		iframe.setAttribute("frameborder","0");
//		HTMLPanel container = new HTMLPanel("");
//		container.getElement().appendChild(iframe);
//		container.addStyleName("modal-iframe-body");
//		
		switch (reportView) {
		case IFRAME:
			
//			iframe.setSrc(event.getUri());
			
			presenter.setInfo(event.getUri(), event.getTitle());
			addToPopupSlot(presenter, true);
//			AppManager.showPopUp(event.getTitle(), container, null,
//					new OnOptionSelected(){
//				@Override
//				public void onSelect(String name) {
//				}
//			}, PopupType.FULLPAGE, "Close");
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

	@Override
	public void onShowMessage(ShowMessageEvent event) {
		getView().setAlertVisible(event.getAlertType(), event.getMessage(),event.isShowDefaultHeading());
	}

	@Override
	public void onLockInteraction(LockInteractionEvent e) {
		if(e.shouldLock()){
			RootPanel.get("loading").getElement().getStyle().setDisplay(Display.INITIAL);
			//getView().showProcessing(true, "Loading...");
		}else{
			RootPanel.get("loading").getElement().getStyle().setDisplay(Display.NONE);
			//getView().showProcessing(false, null);
		}
	}

}
