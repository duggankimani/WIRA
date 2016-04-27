package com.duggan.workflow.client.ui;

import com.duggan.workflow.client.ui.popup.ModalPopup;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
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
	@UiField Element spnSubject;
	@UiField HTMLPanel popoverPanel;
	@UiField Element disconnectionText;
	
	ModalPopup popup = new ModalPopup();
	
	Timer timer = new Timer() {
		
		@Override
		public void run() {
			hideAlert();
		}
	};

	
	
	@Inject
	public MainPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		loadingtext.setId("loading-text");
		popoverPanel.add(popup);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
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
	public void showProcessing(boolean processing,String message) {
		if(processing){
			if(message!=null){
				loadingtext.setInnerText(message);
			}
			loadingtext.removeClassName("hide");
		}else{
			loadingtext.setInnerText("Loading ...");
			loadingtext.addClassName("hide");
		}
	}
	
	@Override
	public void setAlertVisible(String subject, String statement,String url){
		divAlert.removeClassName("hidden");
		spnAlertContent.setInnerText(statement);
		spnSubject.setInnerText(subject);
		aView.setHref(url);
		timer.cancel();
		timer.schedule(10000);
	}
	
	public void hideAlert(){
		divAlert.addClassName("hidden");
	}

	@Override
	public void showDisconnectionMessage(String message) {
		if(message==null){
			message = "Cannot connect to server....";
		}
		disconnectionText.setInnerText(message);
		disconnectionText.removeClassName("hide");
	}

	@Override
	public void clearDisconnectionMsg() {
		disconnectionText.addClassName("hide");
	}

	@Override
	public ModalPopup getModalPopup() {
		return getModalPopup(false);
	}
	
	@Override
	public ModalPopup getModalPopup(boolean reinstantiate) {
		if(reinstantiate){
			popoverPanel.clear();
			popup = new ModalPopup();
			popoverPanel.add(popup);
		}
		return popup;
	}

}
