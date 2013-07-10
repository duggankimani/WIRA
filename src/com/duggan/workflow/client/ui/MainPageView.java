package com.duggan.workflow.client.ui;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.duggan.workflow.client.ui.MainPagePresenter.*;

public class MainPageView extends ViewImpl implements MainPagePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, MainPageView> {
	}

	@UiField HTMLPanel pHeader;
	
	@UiField HTMLPanel pContainer;
	
	@UiField HTMLPanel tabsContainer;
	
	@Inject
	public MainPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot==MainPagePresenter.HEADER_content){
			pHeader.clear();
			
			if(content!=null){
				pHeader.add(content);
			}			
		}else if(slot==MainPagePresenter.CONTENT_SLOT){
			pContainer.clear();
			
			if(content!=null){
				pContainer.add(content);
			}
			
		}
		else if(slot==TABS_content){
			tabsContainer.clear();
			
			if(content!=null){
				tabsContainer.add(content);
			}
			
		}else{
			super.setInSlot(slot, content);
		}
	}
}
