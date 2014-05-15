package com.duggan.workflow.client.ui.admin;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class TabPanel extends Composite implements
		com.gwtplatform.mvp.client.TabPanel {

	private static TabPanelUiBinder uiBinder = GWT
			.create(TabPanelUiBinder.class);

	interface TabPanelUiBinder extends UiBinder<Widget, TabPanel> {
	}

	Tab currentActiveTab;
	@UiField BulletListPanel tabPanel;
	@UiField HTMLPanel tabContent;

	@Inject PlaceManager placeManager;
	
	@Inject
	public TabPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private final List<TabItem> tabList = new ArrayList<TabItem>();

	@Override
	public Tab addTab(TabData tabData, String historyToken) {
		TabItem newTab = createNewTab(tabData);
		int beforeIndex;
		for (beforeIndex = 0; beforeIndex < tabList.size(); ++beforeIndex) {
			if (newTab.getPriority() < tabList.get(beforeIndex).getPriority()) {
				break;
			}
		}

		tabPanel.insert(newTab.asWidget(), beforeIndex);
		tabList.add(beforeIndex, newTab);
		newTab.setTargetHistoryToken(historyToken);
		setTabVisibility(newTab);
		return newTab;
	}

	private TabItem createNewTab(TabData tabData) {
		TabItem item = new TabItem(tabData);
		return item;
	}

	@Override
	public void removeTab(Tab tab) {
		tabPanel.getElement().removeChild(tab.asWidget().getElement());
		tabList.remove(tab);
	}

	@Override
	public void removeTabs() {
		for (Tab tab : tabList) {
			tabPanel.getElement().removeChild(tab.asWidget().getElement());
		}
		tabList.clear();
	}

	@Override
	public void setActiveTab(Tab tab) {
		if (currentActiveTab != null) {
			currentActiveTab.deactivate();
		}
		if (tab != null) {
			tab.activate();
		}
		currentActiveTab = tab;
	}

	//@Override
	public void changeTab(Tab tab, TabData tabData, String historyToken) {
		tab.setText(tabData.getLabel());
		tab.setTargetHistoryToken(historyToken);
	}

	/**
	 * Sets the content displayed in the main panel.
	 * 
	 * @param panelContent
	 *            The {@link IsWidget} to set in the main panel, or {@code null}
	 *            to clear the panel.
	 */
	public void setPanelContent(IsWidget panelContent) {
		tabContent.clear();
		if (panelContent != null) {
			tabContent.add(panelContent);
		}
	}

	/**
	 * Ensures that all tabs are visible or hidden as they should.
	 */
	public void refreshTabs() {
		for (TabItem tab : tabList) {
			setTabVisibility(tab);
		}
	}

	/**
	 * Ensures the specified tab is visible or hidden as it should.
	 * 
	 * @param tab
	 *            The {@link TabItem} to check.
	 */
	private void setTabVisibility(TabItem tab) {
		
		//boolean visible = (tab == currentActiveTab) || tab.canUserAccess();
		//tab.setVisible(visible);
	}

}
