package com.duggan.workflow.client.ui;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.error.ErrorPresenter;
import com.duggan.workflow.client.ui.events.AdminPageLoadEvent;
import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ErrorEvent.ErrorHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.duggan.workflow.client.ui.header.HeaderPresenter;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
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
		Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> implements ErrorHandler, ProcessingCompletedHandler, ProcessingHandler{

	public interface MyView extends View {

		void showProcessing(boolean processing);
		
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
	
	LayoutPanel l;
	HTMLPanel p;
	
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
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(HEADER_content, headerPresenter);	
	}
	
	@Override
	public void onError(final ErrorEvent event) {
		errorFactory.get(new ServiceCallback<ErrorPresenter>() {
			@Override
			public void processResult(ErrorPresenter result) {
				String message = event.getMessage();
				
				result.setMessage(message, event.getId());
				
				MainPagePresenter.this.addToPopupSlot(result);
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
		getView().showProcessing(true);
	}

	@Override
	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showProcessing(false);
	}
}
