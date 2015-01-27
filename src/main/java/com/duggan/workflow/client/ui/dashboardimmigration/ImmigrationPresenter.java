package com.duggan.workflow.client.ui.dashboardimmigration;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ImmigrationPresenter extends
		Presenter<ImmigrationPresenter.IImmigrationView, ImmigrationPresenter.IImmigrationProxy> {

	public interface IImmigrationView extends View {
		void bind();
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.reports)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IImmigrationProxy extends ProxyPlace<ImmigrationPresenter> {
	}

	@Inject DispatchAsync requestHelper;

	@Inject
	public ImmigrationPresenter(final EventBus eventBus, final IImmigrationView view,
			final IImmigrationProxy proxy){
		super(eventBus, view, proxy,MainPagePresenter.CONTENT_SLOT);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		Window.setTitle("Dashboard");
	}
	 
	@Override
	protected void onBind() {
		super.onBind();
		getView().bind();
	}
}
