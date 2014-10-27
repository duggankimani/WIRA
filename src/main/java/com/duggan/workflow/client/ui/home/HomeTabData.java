package com.duggan.workflow.client.ui.home;

import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

public class HomeTabData extends TabDataExt{

	String itemStyle=null;
	String key=null;
	public HomeTabData(String key,String label,String itemStyle, float priority,
			Gatekeeper gateKeeper) {
		super(label, "", priority, gateKeeper);
		this.itemStyle=itemStyle;
		this.key=key;
	}
	
	public HomeTabData(String key,String label,String itemStyle, float priority,
			Gatekeeper gateKeeper, boolean isDisplayLink){
		super(label, "", priority, gateKeeper,isDisplayLink);
		this.itemStyle=itemStyle;
		this.key=key;
	}
	
	public String getKey(){
		return key;
	}
	
}
