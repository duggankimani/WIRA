package com.duggan.workflow.client.ui.activityfeed.components;

import com.duggan.workflow.client.ui.images.ImageResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CarouselItem extends Composite {

	private static CarouselItemUiBinder uiBinder = GWT
			.create(CarouselItemUiBinder.class);
	
	@UiField HTMLPanel divWrapper;
	@UiField Image  imgScreenshot;
	@UiField Element  divText;

	interface CarouselItemUiBinder extends UiBinder<Widget, CarouselItem> {
	}

	public CarouselItem() {
		initWidget(uiBinder.createAndBindUi(this));
//		imgScreenshot.getElement().getStyle().setWidth(443,Unit.PX);
//		imgScreenshot.getElement().getStyle().setHeight(251,Unit.PX);
		
	}
	
	
	public CarouselItem(boolean isActive, ImageResource image, String content){
		this();
		if(isActive){
			divWrapper.getElement().addClassName("active");
		}
		imgScreenshot.setResource(image);
		imgScreenshot.setPixelSize(443, 251);
		divText.setInnerHTML(content);
		
	}

}
