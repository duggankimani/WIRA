package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class TabPanel extends AbstractTabPanel {

	private static TabPanelUiBinder uiBinder = GWT
			.create(TabPanelUiBinder.class);

	interface TabPanelUiBinder extends UiBinder<Widget, TabPanel> {
	}

	@UiField BulletListPanel linksPanel;
	@UiField HTMLPanel tabContent;
	@Inject PlaceManager placeManager;
	@UiField SpanElement spanTitle;
	@UiField SpanElement iconTitle;
	
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
	protected Tab createNewTab(TabData tabData) {
		IconTabItem item = new IconTabItem(tabData);
		return item;
	}

	@Override
	public BulletListPanel getLinksPanel() {
		return linksPanel;
	}
	
	@Override
	public void setActiveTab(Tab tab) {
		super.setActiveTab(tab);
		
		iconTitle.setClassName(((IconTabItem)tab).getTabData().getIconStyle());
		spanTitle.setInnerText(((AbstractTabItem)tab).getTabData().getLabel());
	}

}
