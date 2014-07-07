package com.duggan.workflow.client.ui.admin;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;

public abstract class AbstractTabPanel extends Composite implements com.gwtplatform.mvp.client.TabPanel{


	protected final List<Tab> tabList = new ArrayList<Tab>();
	protected Tab currentActiveTab;
	
	public AbstractTabPanel(){
	}
	
	public abstract BulletListPanel getLinksPanel();
	
	@Override
	public Tab addTab(TabData tabData, String historyToken) {
		Tab newTab = createNewTab(tabData);
		int beforeIndex;
		for (beforeIndex = 0; beforeIndex < tabList.size(); ++beforeIndex) {
			if (newTab.getPriority() < tabList.get(beforeIndex).getPriority()) {
				break;
			}
		}

		getLinksPanel().insert(newTab.asWidget(), beforeIndex);
		tabList.add(beforeIndex, newTab);
		newTab.setTargetHistoryToken(historyToken);
		setTabVisibility(newTab);
		return newTab;
	}

	protected abstract Tab createNewTab(TabData tabData);

	@Override
	public void removeTab(Tab tab) {
		getLinksPanel().getElement().removeChild(tab.asWidget().getElement());
		tabList.remove(tab);
	}

	@Override
	public void removeTabs() {
		for (Tab tab : tabList) {
			getLinksPanel().getElement().removeChild(tab.asWidget().getElement());
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
	public abstract void setPanelContent(IsWidget panelContent);

	/**
	 * Ensures that all tabs are visible or hidden as they should.
	 */
	public void refreshTabs() {
		for (Tab tab : tabList) {
			setTabVisibility(tab);
		}
	}

	/**
	 * Ensures the specified tab is visible or hidden as it should.
	 * 
	 * @param tab
	 *            The {@link IconTabItem} to check.
	 */
	protected void setTabVisibility(Tab tab) {
		AbstractTabItem item = ((AbstractTabItem)tab);
		item.show(item.getTabData().isDisplayed());
	}

}
