package com.duggan.workflow.client.ui;


import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.error.ErrorPresenter;
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
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

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

	@ContentSlot
	public static final Type<RevealContentHandler<?>> HEADER_content = new Type<RevealContentHandler<?>>();
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> CONTENT_SLOT = new Type<RevealContentHandler<?>>();

	@Inject HeaderPresenter headerPresenter;
		
	IndirectProvider<ErrorPresenter> errorFactory;
	
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;

	@Inject IFrameDataPresenter presenter;
	
	@Inject
	public MainPagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,Provider<ErrorPresenter> provider) {
		super(eventBus, view, proxy);
		this.errorFactory = new StandardProvider<ErrorPresenter>(provider);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
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
		errorFactory.get(new ServiceCallback<ErrorPresenter>() {
			@Override
			public void processResult(ErrorPresenter result) {
				String message = event.getMessage();
				
				result.setMessage(message, event.getId());
				
				MainPagePresenter.this.addToPopupSlot(result,true);
				
			}
		});
	}

	@Override
	public void setInSlot(Object slot, PresenterWidget<?> content) {
		super.setInSlot(slot, content);
		
		if(slot==CONTENT_SLOT){
			if(content!=null && content instanceof AdminHomePresenter){
				fireEvent(new AdminPageLoadEvent(true));
			}else{
				fireEvent(new AdminPageLoadEvent(false));
			}
		}
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
