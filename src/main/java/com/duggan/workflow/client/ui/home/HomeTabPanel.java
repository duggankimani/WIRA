package com.duggan.workflow.client.ui.home;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.ui.admin.AbstractTabPanel;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;

public class HomeTabPanel extends AbstractTabPanel {

	private static HomeTabPanelUiBinder uiBinder = GWT
			.create(HomeTabPanelUiBinder.class);

	interface HomeTabPanelUiBinder extends UiBinder<Widget, HomeTabPanel> {
	}
	
	@UiField BulletListPanel linksPanel;
	
	public HomeTabPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPanelContent(IsWidget panelContent) {
		//Moved this to the Home View
	}
	
	@Override
	public BulletListPanel getLinksPanel() {
		return linksPanel;
	}

	@Override
	protected Tab createNewTab(TabData tabData) {
		
		return new TabItem(tabData);
	}

	public void changeTab(TaskType type, String text) {
		for(Tab tab: tabList){
			TabItem item = (TabItem)tab;
			if(item.isFor(type)){
				item.setText(text);
			}
		}
	}
}
