package com.duggan.workflow.client.ui.admin;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;

public abstract class AbstractTabItem extends Composite implements Tab{

	protected TabData data;
	
	public AbstractTabItem(TabData data){
		this.data=data;
	}
	
	public void show(boolean show){
		if(show)
			getLiContainer().removeStyleName("hide");
		else
			getLiContainer().addStyleName("hide");
	}
	
	public void setActive(boolean active){
		if(active){
			getLiContainer().addStyleName("active");
		}else{
			getLiContainer().removeStyleName("active");
		}
		
	}
	
	public abstract Widget getLiContainer();
	public abstract Anchor getLink();
	public abstract Element getNameEl();
	
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

		return getNameEl().getInnerText();
	}

	@Override
	public void setTargetHistoryToken(String historyToken) {
		getLink().setHref("#"+historyToken);
	}

	@Override
	public void setText(String text) {
		getNameEl().setInnerText(text);
	}

	public boolean canUserAccess() {
		return true;
	}
	
	public TabDataExt getTabData(){
		return (TabDataExt)data;
	}
}
