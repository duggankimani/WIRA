package com.duggan.workflow.client.ui.activityfeed;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.duggan.workflow.client.place.NameTokens;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.duggan.workflow.client.ui.events.ActivitiesSelectedEvent;
import com.duggan.workflow.client.ui.home.HomePresenter;

public class ActivitiesPresenter extends
		Presenter<ActivitiesPresenter.MyView, ActivitiesPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.activities)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<ActivitiesPresenter> {
	}

	@Inject
	public ActivitiesPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, HomePresenter.ACTIVITIES_SLOT, this);
	}
	 @Override
	public void prepareFromRequest(PlaceRequest request) {
		 super.prepareFromRequest(request);
		 //fireEvent(new ActivitiesSelectedEvent());
	}
	@Override
	protected void onBind() {
		super.onBind();
	}
}
