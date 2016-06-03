package com.duggan.workflow.client.ui.admin;

import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

public class TabDataExt extends TabDataBasic {

	private String iconStyle;
	private int count=0;
	Gatekeeper gateKeeper;
	boolean isDisplayLink=true;
	
	public TabDataExt(String label,String iconStyle,
	                  float priority,
	                  Gatekeeper gateKeeper) {
	    super(label, priority);
	    this.iconStyle = iconStyle;
	    this.gateKeeper = gateKeeper;
	}
	
	public TabDataExt(String label,String iconStyle,
            float priority,
            Gatekeeper gateKeeper, boolean isDisplayLink) {
		this(label, iconStyle, priority, gateKeeper);
		this.isDisplayLink = isDisplayLink;
	}
	
	public TabDataExt(String label,int count,
            float priority,
            Gatekeeper gatekeeper) {
		super(label, priority);
		this.count=count;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public int getCount() {
		return count;
	}
	

	public boolean canReveal() {
		return gateKeeper.canReveal();
	}
	
	public boolean canUserAccess() {
		return gateKeeper.canReveal() && isDisplayLink;
	}

	public Gatekeeper getGateKeeper() {
		return gateKeeper;
	}
	
}
