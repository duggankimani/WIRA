package com.duggan.workflow.client.ui.popup;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class GenericPopupPresenter extends
		PresenterWidget<GenericPopupPresenter.MyView> {

	public interface MyView extends PopupView {

		void setHeader(String header);

		PopupPanel getPopUpPanel();

		void addStyleName(String customPopupStyle);

		void removeStyleName(String customPopupStyle);
	}

	@Inject
	DispatchAsync dispatcher;

	@ContentSlot
	public static final Type<RevealContentHandler<?>> BODY_SLOT = new Type<RevealContentHandler<?>>();
	
	public static final Object BUTTON_SLOT = new Object();
	
	@Inject
	public GenericPopupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
		
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	public void setHeader(String header) {
		getView().setHeader(header);
	}
}
