package com.duggan.workflow.client.ui;

import com.duggan.workflow.client.ui.popup.ModalPopup;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ApplicationView> {
	}

	@UiField
	HTMLPanel pHeader;
	@UiField
	HTMLPanel pContainer;
	@UiField
	SpanElement loadingtext;
	@UiField
	DivElement divAlert;
	@UiField
	SpanElement spnAlertContent;
	@UiField
	Anchor aView;
	@UiField
	Element spnSubject;
	@UiField
	HTMLPanel popoverPanel;
	@UiField
	Element disconnectionText;
	@UiField
	Element elMobileLoader;

	@UiField
	Element divGenericAlerts;
	
	ModalPopup popup = new ModalPopup();

	Timer timer = new Timer() {

		@Override
		public void run() {
			hideAlert();
		}
	};

	@Inject
	public ApplicationView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		loadingtext.setId("loading-text");
		popoverPanel.add(popup);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == ApplicationPresenter.HEADER_content) {
			pHeader.clear();

			if (content != null) {
				pHeader.add(content);
			}
		} else if (slot == ApplicationPresenter.CONTENT_SLOT) {
			pContainer.clear();

			if (content != null) {
				pContainer.add(content);
			}

		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void showProcessing(boolean processing, String message) {
		if (processing) {
			if (message != null) {
				loadingtext.setInnerText(message);
			}
			loadingtext.removeClassName("hide");
			elMobileLoader.removeClassName("hide");
		} else {
			loadingtext.setInnerText("Loading ...");
			loadingtext.addClassName("hide");
			elMobileLoader.addClassName("hide");
		}
	}

	@Override
	public void setAlertVisible(String subject, String statement, String url) {
		divAlert.removeClassName("hidden");
		spnAlertContent.setInnerText(statement);
		spnSubject.setInnerText(subject);
		aView.setHref(url);
		timer.cancel();
		timer.schedule(5000);
		aView.setText("View Document");
	}

	public void setAlertVisible(AlertType alertType, String statement,boolean showDefaultHeading) {
		divGenericAlerts.removeClassName("hide");
		divGenericAlerts.removeClassName("alert-success");
		divGenericAlerts.removeClassName("alert-info");
		divGenericAlerts.removeClassName("alert-warning");
		divGenericAlerts.removeClassName("alert-danger");

		switch (alertType) {
		case DANGER:
			divGenericAlerts.addClassName("alert-danger");
			divGenericAlerts.setInnerHTML((showDefaultHeading?"<strong>Danger!</strong> ":"")+
					statement);
			break;
		case SUCCESS:
			divGenericAlerts.addClassName("alert-success");
			divGenericAlerts.setInnerHTML((showDefaultHeading?"<strong>Success!</strong> ":"")+
					statement);
			break;

		case WARNING:
			divGenericAlerts.addClassName("alert-warning");
			divGenericAlerts.setInnerHTML((showDefaultHeading?"<strong>Warning!</strong> ":"")+
					statement);
			break;

		default:
			divGenericAlerts.addClassName("alert-info");
			divGenericAlerts.setInnerHTML((showDefaultHeading?"<strong>Info!</strong> ":"")+
			statement);
			break;
		}
		timer.cancel();
		timer.schedule(3000);
	}

	public void hideAlert() {
		divAlert.addClassName("hidden");
		divGenericAlerts.addClassName("hide");
	}

	@Override
	public void showDisconnectionMessage(String message) {
		if (message == null) {
			message = "Cannot connect to server....";
		}
		disconnectionText.setInnerText(message);
		disconnectionText.removeClassName("hide");
	}

	@Override
	public void clearDisconnectionMsg() {
		disconnectionText.addClassName("hide");
	}

	@Override
	public ModalPopup getModalPopup() {
		return getModalPopup(false);
	}

	@Override
	public ModalPopup getModalPopup(boolean reinstantiate) {
		if (reinstantiate) {
			popoverPanel.clear();
			popup = new ModalPopup();
			popoverPanel.add(popup);
		}
		return popup;
	}

}
