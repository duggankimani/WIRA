package com.duggan.workflow.client.ui;

import com.gwtplatform.mvp.client.PopupView;

public class OptionControl implements OnOptionSelected{

	PopupView parentPanel;
	
	@Override
	public void onSelect(String name) {
	}
	
	public void setPopupView(PopupView panel){
		this.parentPanel = panel;
	}
	
	public void hide(){
		parentPanel.hide();
	}

}
