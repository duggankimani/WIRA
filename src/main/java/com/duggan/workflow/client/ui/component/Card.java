package com.duggan.workflow.client.ui.component;

import com.duggan.workflow.shared.model.DocumentType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Card extends Composite {

	private static CardUiBinder uiBinder = GWT.create(CardUiBinder.class);

	interface CardUiBinder extends UiBinder<Widget, Card> {
	}
	
	@UiField Element spnName;
	@UiField AnchorElement aProcess;
	@UiField AnchorElement aAdd;
	@UiField Element cardImage;
	@UiField Element cardImageIcon;

	String iconStyle="icon-pencil";
	String backgroundColor = "#0096B3";
	
	public Card() {
		initWidget(uiBinder.createAndBindUi(this));
		setIconStyle(iconStyle);
		setBackGroundColor(backgroundColor);
	}

	public Card(DocumentType type) {
		this();
		setDisplay(type.getDisplayName());
		if(type.getIconStyle()!=null){
			setIconStyle(type.getIconStyle());
		}
		
		if(type.getBackgroundColor()!=null){
			setBackGroundColor(type.getBackgroundColor());
		}

	}
	
	public void setDisplay(String name){
		spnName.setInnerText(name);
	}

	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
		cardImageIcon.setClassName("icon-large mid-icon "+iconStyle);
	}

	public void setBackGroundColor(String backGroundColor) {
		this.backgroundColor= backGroundColor;
		cardImage.getStyle().setBackgroundColor(backGroundColor);
	}

	public String getBackGroundColor() {
		return backgroundColor;
	}

	public String getIconStyle() {
		return iconStyle;
	}

}
