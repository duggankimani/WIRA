package com.duggan.workflow.client.ui.home;

import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

public class HomeTabData extends TabDataExt{

	String itemStyle=null;
	
	public HomeTabData(String label,String itemStyle, String iconStyle, float priority,
			Gatekeeper gatekeeper) {
		super(label, iconStyle, priority, gatekeeper);
	}
}
