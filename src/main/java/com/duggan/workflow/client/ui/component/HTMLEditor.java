package com.duggan.workflow.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class HTMLEditor extends Composite implements HasText {

	private static HTMLEditorUiBinder uiBinder = GWT
			.create(HTMLEditorUiBinder.class);

	interface HTMLEditorUiBinder extends UiBinder<Widget, HTMLEditor> {
	}

	@UiField TextArea txtHTML;
	
	public HTMLEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}

	public HTMLEditor(String html) {
		initWidget(uiBinder.createAndBindUi(this));
		setText(html);
	}

	public void setText(String text) {
		txtHTML.setValue(text);
	}

	public String getText() {
		return txtHTML.getValue();
	}

}
