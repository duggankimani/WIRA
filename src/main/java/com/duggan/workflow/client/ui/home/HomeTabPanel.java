package com.duggan.workflow.client.ui.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class HomeTabPanel extends Composite implements HasText {

	private static HomeTabPanelUiBinder uiBinder = GWT
			.create(HomeTabPanelUiBinder.class);

	interface HomeTabPanelUiBinder extends UiBinder<Widget, HomeTabPanel> {
	}
	
	@UiField Anchor btnAdd;
	
	@UiField HTMLPanel wholeContainer;
	@UiField HTMLPanel mainContainer;

	public HomeTabPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		btnAdd.getElement().setAttribute("data-toggle", "dropdown");
		
	}

	@UiField
	Button button;

	public HomeTabPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

	public void setText(String text) {
		button.setText(text);
	}

	public String getText() {
		return button.getText();
	}

	public HasClickHandlers getAddButton() {
		return btnAdd;
	}
}
