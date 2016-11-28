package com.duggan.workflow.client.ui.error;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.ApplicationPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class NotfoundPresenter extends
		Presenter<NotfoundPresenter.MyView, NotfoundPresenter.MyProxy> {

	private PlaceManager placeManager;
	public interface MyView extends View {
	}

	@NoGatekeeper
	@ProxyCodeSplit
	@NameToken(NameTokens.error404)
	public interface MyProxy extends ProxyPlace<NotfoundPresenter> {
	}

	@Inject
	public NotfoundPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, ApplicationPresenter.CONTENT_SLOT, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		if(!AppContext.isLoggedIn()){
			placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.splash).build());
		}
	}
	@Override
	protected void onBind() {
		super.onBind();
	}
}
