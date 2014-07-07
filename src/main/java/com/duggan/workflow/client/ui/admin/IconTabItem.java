package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.ui.component.BulletPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.TabData;

public class IconTabItem extends AbstractTabItem{

	private static TabItemUiBinder uiBinder = GWT.create(TabItemUiBinder.class);

	interface TabItemUiBinder extends UiBinder<Widget, IconTabItem> {
	}

	@UiField BulletPanel liContainter;
	@UiField SpanElement spnName;
	@UiField Anchor aLink;
	@UiField Element icon;
	
	public IconTabItem(TabData tabData) {
		super(tabData);
		initWidget(uiBinder.createAndBindUi(this));
		TabDataExt data = (TabDataExt)tabData;
		icon.setClassName(data.getIconStyle());
		setText(tabData.getLabel());
	}

	@Override
	public Widget getLiContainer() {
		
		return liContainter;
	}

	@Override
	public Anchor getLink() {
		
		return aLink;
	}

	@Override
	public Element getNameEl() {
		return spnName;
	}
	
}
