package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.ui.component.BulletPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;

public class TabItem extends Composite implements Tab {

	private static TabItemUiBinder uiBinder = GWT.create(TabItemUiBinder.class);

	interface TabItemUiBinder extends UiBinder<Widget, TabItem> {
	}

	@UiField BulletPanel liContainter;
	@UiField SpanElement spnName;
	@UiField Anchor aLink;
	@UiField Element icon;
	TabData data;
	
	public TabItem(TabData tabData) {
		initWidget(uiBinder.createAndBindUi(this));
		TabDataExt data = (TabDataExt)tabData;
		this.data = data;		
		icon.setClassName(data.getIconStyle());
		setText(data.getLabel());
	}
	
	public void show(boolean show){
		if(show)
			liContainter.removeStyleName("hide");
		else
			liContainter.addStyleName("hide");
	}
	
	public void setActive(boolean active){
		if(active){
			liContainter.addStyleName("active");
		}else{
			liContainter.removeStyleName("active");
		}
		
	}

	@Override
	public void activate() {
		setActive(true);
	}

	@Override
	public void deactivate() {
		setActive(false);
	}

	@Override
	public float getPriority() {
		
		return data.getPriority();
	}

	@Override
	public String getText() {

		return spnName.getInnerText();
	}

	@Override
	public void setTargetHistoryToken(String historyToken) {
		aLink.setHref("#"+historyToken);
	}

	@Override
	public void setText(String text) {
		spnName.setInnerText(text);
	}

	public boolean canUserAccess() {
		return true;
	}

}
