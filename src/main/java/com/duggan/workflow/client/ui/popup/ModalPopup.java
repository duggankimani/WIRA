package com.duggan.workflow.client.ui.popup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PopupViewCloseHandler;
import com.gwtplatform.mvp.client.view.PopupPositioner;

public class ModalPopup extends Composite implements PopupView {

	private static ModalPopupUiBinder uiBinder = GWT
			.create(ModalPopupUiBinder.class);

	interface ModalPopupUiBinder extends UiBinder<Widget, ModalPopup> {
	}

	@UiField
	HTMLPanel modalPanel;

	@UiField
	HTMLPanel panelContent;
	@UiField
	HTMLPanel buttonBar;
	@UiField
	Element elHeader;

	public ModalPopup() {
		initWidget(uiBinder.createAndBindUi(this));
		modalPanel.getElement().setId("wiraModal");
		modalPanel.getElement().setAttribute("tabindex", "-1");
		modalPanel.getElement().setAttribute("role", "dialog");
		modalPanel.getElement().setAttribute("aria-labelledby", "myModalLabel");
		modalPanel.getElement().setAttribute("aria-hidden", "true");
		modalPanel.getElement().setAttribute("data-backdrop", "static");
		modalPanel.getElement().setAttribute("data-keyboard", "false");
	}

	public void setHeader(String header) {
		elHeader.setInnerText(header);
	}

	public void setContent(Widget content) {
		panelContent.clear();
		if (content != null) {
			panelContent.add(content);
		}

	}

	@Override
	public void addToSlot(Object slot, IsWidget content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeFromSlot(Object slot, IsWidget content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		hidePopup();
	}

	private static native void hidePopup()/*-{
											$wnd.jQuery('#wiraModal').modal('hide');
											}-*/;

	@Override
	public void setAutoHideOnNavigationEventEnabled(boolean autoHide) {

	}

	@Override
	public void setCloseHandler(PopupViewCloseHandler popupViewCloseHandler) {

	}

	@Override
	public void show() {
		showPopup();
	}

	private static native void showPopup()/*-{
											$wnd.jQuery('#wiraModal').modal('show');
											}-*/;

	@Override
	public void showAndReposition() {

	}

	@Override
	public void setPopupPositioner(PopupPositioner popupPositioner) {

	}

	@Override
	public void setPosition(int left, int top) {

	}

	@Override
	public void center() {

	}

	public void addButton(Anchor aLnk) {
		buttonBar.add(aLnk);
	}

	public void clear() {
		buttonBar.clear();
		elHeader.setInnerText("");
	}

}
