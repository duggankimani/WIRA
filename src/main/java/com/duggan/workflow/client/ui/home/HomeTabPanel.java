package com.duggan.workflow.client.ui.home;

import java.util.List;

import com.duggan.workflow.client.ui.admin.AbstractTabPanel;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class HomeTabPanel extends AbstractTabPanel {

	private static HomeTabPanelUiBinder uiBinder = GWT
			.create(HomeTabPanelUiBinder.class);

	interface HomeTabPanelUiBinder extends UiBinder<Widget, HomeTabPanel> {
	}
	
	@UiField BulletListPanel linksPanel;
	
	@UiField HTMLPanel tabContent;

	@Inject PlaceManager placeManager;
	
	@UiField Anchor btnAdd;
	
	@UiField HTMLPanel mainContainer;

	public HomeTabPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		btnAdd.getElement().setAttribute("data-toggle", "dropdown");
		
	}

	public HasClickHandlers getAddButton() {
		return btnAdd;
	}
	
	public void setPanelContent(IsWidget panelContent) {
		tabContent.clear();
		if (panelContent != null) {
			tabContent.add(panelContent);
		}
	}
	
	@Override
	public Tab addTab(TabData tabData, String historyToken) {
		
		if(tabData instanceof CompositeTabData){
			CompositeTabData data = (CompositeTabData)tabData;
			List<TabData> lst = data.getTabData();
			for(TabData d: lst){
				//addTab(d, d.get);
			}
			return null;
		}else{
			return super.addTab(tabData, historyToken);
		}
	}

	@Override
	public BulletListPanel getLinksPanel() {
		return linksPanel;
	}
}
