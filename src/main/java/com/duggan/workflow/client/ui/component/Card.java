package com.duggan.workflow.client.ui.component;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.events.CreateDocumentEvent;
import com.duggan.workflow.shared.model.DocumentType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
	@UiField AnchorElement aNavigate;
	@UiField AnchorElement aCount;
	
	@UiField Element spnCount;

	String iconStyle="icon-pencil";
	String backgroundColor = "#0096B3";
	private DocumentType docType;
	
	public Card() {
		initWidget(uiBinder.createAndBindUi(this));
		setIconStyle(iconStyle);
		setBackGroundColor(backgroundColor);
		registerEvents(aAdd);
	}

	public Card(DocumentType type) {
		this();
		this.docType = type;
		setDisplay(type.getDisplayName());
		if(type.getIconStyle()!=null){
			setIconStyle(type.getIconStyle());
		}
		
		if(type.getBackgroundColor()!=null){
			setBackGroundColor(type.getBackgroundColor());
		}
		
		if(type.getProcessRefId()!=null){
			aProcess.setHref("#/activities/"+type.getProcessRefId());
			aNavigate.setHref("#/activities/"+type.getProcessRefId());
		}

		if(type.getInboxCount()==0){
			spnCount.addClassName("hide");
		}else{
			spnCount.setInnerText(""+type.getInboxCount()+" new");
			spnCount.removeClassName("hide");
			aCount.setHref("#/inbox/all/"+type.getProcessRefId());
		}
	}

	public void setDisplay(String name){
		spnName.setInnerText(name);
		spnName.setTitle(name);
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
	
	public void addDoc(){
		AppContext.getEventBus().fireEvent(new CreateDocumentEvent(docType));
	}
	
	public native void registerEvents(Element el)/*-{
		var instance = this;
		$wnd.jQuery($doc).ready(function(){
			$wnd.jQuery(el).click(function(){
				instance.@com.duggan.workflow.client.ui.component.Card::addDoc()();
			});
		})
	}-*/;
}
