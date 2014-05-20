package com.duggan.workflow.client.ui.admin;

import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

public class TabDataExt extends TabDataBasic {

	private final Gatekeeper gatekeeper;
	
	private String iconStyle;
	public TabDataExt(String label,String iconStyle,
	                  float priority,
	                  Gatekeeper gatekeeper) {
	    super(label, priority);
	    this.iconStyle = iconStyle;
	    this.gatekeeper = gatekeeper;
	}
	
	public Gatekeeper getGatekeeper() {
	    return gatekeeper;
	}

	public String getIconStyle() {
		return iconStyle;
	}
	

}
