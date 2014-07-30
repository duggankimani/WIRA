package com.duggan.workflow.client.ui.admin;

import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

public class TabDataExt extends TabDataBasic {

	private final Gatekeeper gatekeeper;
	
	private String iconStyle;
	private int count=0;
	private boolean isDisplayed=true;
	public TabDataExt(String label,String iconStyle,
	                  float priority,
	                  Gatekeeper gatekeeper) {
	    super(label, priority);
	    this.iconStyle = iconStyle;
	    this.gatekeeper = gatekeeper;
	}
	
	public TabDataExt(String label,int count,
            float priority,
            Gatekeeper gatekeeper) {
		super(label, priority);
		this.count=count;
		this.gatekeeper = gatekeeper;
	}
	
	public Gatekeeper getGatekeeper() {
	    return gatekeeper;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public int getCount() {
		return count;
	}
	
	public void setDisplayed(boolean isDisplayed){
		this.isDisplayed = isDisplayed;
	}

	public boolean isDisplayed(){
		return isDisplayed;
	}
}
