package com.duggan.workflow.client.ui;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainPageView extends ViewImpl implements MainPagePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, MainPageView> {
	}

	@UiField HTMLPanel pHeader;
	@UiField HTMLPanel pContainer;
	@UiField SpanElement loadingtext;
	@UiField DivElement divAlert;
	@UiField SpanElement spnAlertContent;
	@UiField Anchor aView;
	
	
	@Inject
	public MainPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		loadingtext.setId("loading-text");
		
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
		else{
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void showProcessing(boolean processing) {
		if(processing){
			loadingtext.removeClassName("hide");
		}else{
			loadingtext.addClassName("hide");
		}
	}
	
	@Override
	public void setAlertVisible(String statement,String url){
		divAlert.removeClassName("hidden");
		spnAlertContent.setInnerText(statement);
		aView.setHref(url);
	}

}
