package com.duggan.workflow.client.ui.home;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;

public class CompositeTabData extends TabDataBasic{

	List<TabData> list = new ArrayList<TabData>();
	
	public CompositeTabData(String label, float priority, TabData ... tabData) {
		super(label, priority);
		
		for(TabData b: tabData){
			list.add(b);
		}
	}
	
	public List<TabData> getTabData(){
		return list;
	}
}
