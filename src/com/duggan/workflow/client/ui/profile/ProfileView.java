package com.duggan.workflow.client.ui.profile;

import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProfileView extends ViewImpl implements ProfilePresenter.MyView {

	private final Widget widget;
	@UiField Uploader uploader;
	@UiField FocusPanel panelPicture;
	@UiField FocusPanel uploadPanel;
	@UiField HTMLPanel panelPassword;
	@UiField DivElement divPassword;
	
	@UiField Button btnPassword;
	@UiField Button btnSave;

	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		uploader.addStyleName("custom-file-input");
		
		panelPicture.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				uploader.removeStyleName("hide");
			}
		});
		
		uploadPanel.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				uploader.addStyleName("hide");
			}
		});
	
		btnPassword.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divPassword.addClassName("hide");
				panelPassword.removeStyleName("hide");
			}
		});
		
		btnSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divPassword.removeClassName("hide");
				panelPassword.addStyleName("hide");
			}
		});
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
