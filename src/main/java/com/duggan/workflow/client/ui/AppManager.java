package com.duggan.workflow.client.ui;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.admin.formbuilder.propertypanel.PropertyPanelPresenter;
import com.duggan.workflow.client.ui.popup.ModalPopup;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewImpl;

public class AppManager {

	@Inject
	static ApplicationPresenter ApplicationPresenter;
	
	@Inject
	static PropertyPanelPresenter propertyPanel;

	public static void showPopUp(String header, String content,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, new InlineLabel(content), onOptionSelected, buttons);
	}
	
	public static void showPopUp(String header, String content,
			final OnOptionSelected onOptionSelected,PopupType type, String... buttons) {
		showPopUp(header, new InlineLabel(content), onOptionSelected,type, buttons);
	}

	public static void showPopUp(String header, Widget widget,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, widget, null, onOptionSelected,PopupType.DEFAULT, buttons);
	}
	
	public static void showPopUp(String header, Widget widget,
			final OnOptionSelected onOptionSelected,PopupType type, String... buttons) {
		showPopUp(header, widget, null, onOptionSelected,type, buttons);
	}
	
	public static void showPopUp(String header, Widget widget,final String customPopupStyle,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, widget, customPopupStyle, onOptionSelected, PopupType.DEFAULT, buttons);
	}
	public static void showPopUp(String header, Widget widget,final String customPopupStyle,
			final OnOptionSelected onOptionSelected,PopupType type, String... buttons) {
		
		final ModalPopup modalPopup = ApplicationPresenter.getView().getModalPopup(true);
		modalPopup.clear();
		
		if(type== PopupType.FULLPAGE){
			modalPopup.addStyleName("full-page-popup");
		}else{
			modalPopup.removeStyleName("full-page-popup");
		}
		
		modalPopup.setHeader(header);
		modalPopup.setContent(widget);
		
		if(customPopupStyle!=null){
			modalPopup.addStyleName(customPopupStyle);
		}
		
		for (final String text : buttons) {
			Anchor aLnk = new Anchor(text);
			aLnk.setStyleName("btn");
//			if (text.equals("Cancel")) {
//				aLnk.setHTML("&nbsp;<i class=\"glyphicon glyphicon-remove\"></i>" + text);
//				aLnk.setStyleName("btn btn-danger pull-left");
//			} else {
//				aLnk.setHTML(text
//						+ "&nbsp;<i class=\"glyphicon glyphicon-double-angle-right\"></i>");
//				aLnk.setStyleName("btn btn-primary pull-right");
//			}

			aLnk.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
										
					if (onOptionSelected instanceof OptionControl) {
						((OptionControl) onOptionSelected)
								.setPopupView((PopupView) modalPopup);
						onOptionSelected.onSelect(text);
					} else {
						modalPopup.hide();
						onOptionSelected.onSelect(text);
					}
					
					
					if(!modalPopup.isVisible() && customPopupStyle!=null){
						modalPopup.removeStyleName(customPopupStyle);
					}
				}
			});
			modalPopup.addButton(aLnk);
		}

		modalPopup.show();
	}

	public static void showPopUp(String header,
			PresenterWidget<ViewImpl> presenter,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, presenter.asWidget(), onOptionSelected, buttons);
	}

	public static void showPropertyPanel(FormModel parent,
			ArrayList<Property> properties) {
		//Bad Fix - For correction 
		propertyPanel.getView().showBody(false, null);
		propertyPanel.setProperties(parent, properties);
		propertyPanel.getView().getPopoverFocus().setFocus(true);
		showPopUp("Properties", propertyPanel.getView().asWidget(), new OnOptionSelected() {
			
			@Override
			public void onSelect(String name) {
				if(name.equals("Save")){
					propertyPanel.save();
				}
			}
		}, "Save", "Cancel");
	}
	
	public static void showCarouselPanel(Widget widget, int[] position,
			boolean isLeft) {
		//propertyPanel.getView().getPopUpContainer().clear();
		propertyPanel.getView().showBody(true, widget);
		

	}

	/**
	 * Returns positions of the modal/popover in Relative to the browser size
	 * 
	 * @param %top, %left
	 * @return top(px),left(px)
	 */
	public static int[] calculatePosition(int top, int left) {

		int[] positions = new int[2];
		// ----Calculate the Size of Screen;
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();

		/* Percentage to the Height and Width */
		double percentTop = (top / 100.0) * height;
		double percentLeft = (left / 100.0) * width;

		positions[0] = (int) percentTop;
		positions[1] = (int) percentLeft;

		return positions;
	}

	public static void hidePopup() {
		final ModalPopup modalPopup = ApplicationPresenter.getView().getModalPopup(false);
		if(modalPopup!=null){
			modalPopup.hide();
		}
	}

}
