package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class TabPanel extends AbstractTabPanel {

	private static TabPanelUiBinder uiBinder = GWT
			.create(TabPanelUiBinder.class);

	interface TabPanelUiBinder extends UiBinder<Widget, TabPanel> {
	}

	@UiField BulletListPanel linksPanel;
	@UiField HTMLPanel tabContent;
	@Inject PlaceManager placeManager;
	
	@Inject
	public TabPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPanelContent(IsWidget panelContent) {
		tabContent.clear();
		if (panelContent != null) {
			tabContent.add(panelContent);
		}
	}

	@Override
	public BulletListPanel getLinksPanel() {
		return linksPanel;
	}

}
