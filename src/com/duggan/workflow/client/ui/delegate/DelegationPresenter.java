package com.duggan.workflow.client.ui.delegate;

import com.duggan.workflow.client.ui.popup.GenericPopupPresenter;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class DelegationPresenter extends
		Presenter<DelegationPresenter.MyView, DelegationPresenter.MyProxy> {
	
	@Inject static GenericPopupPresenter popupPresenter;
	
	public interface MyView extends View {
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DelegationPresenter> {
	}

	@Inject
	public DelegationPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		popupPresenter.setHeader("Hallo");
		popupPresenter.setInSlot(GenericPopupPresenter.BODY_SLOT, null);
		popupPresenter.setInSlot(GenericPopupPresenter.BUTTON_SLOT, null);
		
	}
}
