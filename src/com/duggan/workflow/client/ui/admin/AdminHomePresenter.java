package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.admin.process.ProcessPresenter;
import com.duggan.workflow.client.ui.admin.processrow.ProcessColumnPresenter;
import com.duggan.workflow.client.ui.events.AdminPageLoadEvent;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class AdminHomePresenter extends
		Presenter<AdminHomePresenter.MyView, AdminHomePresenter.MyProxy> {

	public interface MyView extends View {
		public Anchor getaNewProcess(); 
	}
	
	
	@ProxyCodeSplit
	@NameToken(NameTokens.adminhome)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<AdminHomePresenter> {
	}
	
	public static final Object TABLE_SLOT = new Object();
	
	IndirectProvider<ProcessPresenter> processFactory;
	IndirectProvider<ProcessColumnPresenter> columnFactory;
	
	@Inject
	public AdminHomePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, Provider<ProcessPresenter> processProvider, Provider<ProcessColumnPresenter> columnProvider) {
		super(eventBus, view, proxy);
		processFactory = new StandardProvider<ProcessPresenter>(processProvider);
		columnFactory= new StandardProvider<ProcessColumnPresenter>(columnProvider);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		 super.prepareFromRequest(request);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getaNewProcess().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				processFactory.get(new ServiceCallback<ProcessPresenter>() {
					@Override
					public void processResult(ProcessPresenter result) {
						addToPopupSlot(result);
					}
				});
				
			}
		});
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		
		setInSlot(TABLE_SLOT, null);
		for(int i=0; i<=5; i++){
			columnFactory.get(new AsyncCallback<ProcessColumnPresenter>() {
			
			@Override
			public void onSuccess(ProcessColumnPresenter result) {
				addToSlot(TABLE_SLOT, result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		   });
		}
	}
}
