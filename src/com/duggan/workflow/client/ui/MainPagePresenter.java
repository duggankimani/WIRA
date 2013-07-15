package com.duggan.workflow.client.ui;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.error.ErrorPresenter;
import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ErrorEvent.ErrorHandler;
import com.duggan.workflow.client.ui.header.HeaderPresenter;
import com.duggan.workflow.client.ui.tasklist.tabs.TabsPresenter;
import com.duggan.workflow.shared.model.CurrentUser;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class MainPagePresenter extends
		Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> implements ErrorHandler{

	public interface MyView extends View {
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
	
	@Inject CurrentUser user;
	
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
//		System.err.println("#####MainPage Bind Called!!");

		//		This is never called
		
//		dispatcher.execute(new GetContextRequest(),
//				new TaskServiceCallback<GetContextRequestResult>() {
//					@Override
//					public void processResult(GetContextRequestResult result) {
//						
//						if(result.getIsValid()){
//
//							user.setValid(result.getIsValid());
//							user.setFullName(result.getUserId());
//							user.setUserId(result.getUserId());
//							
//						}
//						
//						setInSlot(HEADER_content, headerPresenter);
//						setInSlot(TABS_content, tabsPresenter);
//						
//					}
//				});
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
				result.setMessage(event.getMessage());
				MainPagePresenter.this.addToPopupSlot(result);
			}
		});
	}

}
