package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.DocumentType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class CardRow extends Composite {

	private static CardRowUiBinder uiBinder = GWT.create(CardRowUiBinder.class);

	interface CardRowUiBinder extends UiBinder<Widget, CardRow> {
	}

	@UiField HTMLPanel rowPanel;
	
	public CardRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public CardRow(ArrayList<DocumentType> docTypes){
		this();
		for(DocumentType type: docTypes){
			HTMLPanel span3 = new HTMLPanel("");
			span3.addStyleName("span3");
			span3.add(new Card(type));
			rowPanel.add(span3);
		}
	}

}
