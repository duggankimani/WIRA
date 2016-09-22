package com.duggan.workflow.client.ui.landingpage;

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

	public Card() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Card(DocumentType type) {
		this();
		spnName.setInnerText(type.getDisplayName());
	}

}
