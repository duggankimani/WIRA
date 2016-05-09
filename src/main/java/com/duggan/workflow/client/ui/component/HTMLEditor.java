package com.duggan.workflow.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;

public class HTMLEditor extends Composite implements HasText {

	private static HTMLEditorUiBinder uiBinder = GWT
			.create(HTMLEditorUiBinder.class);

	interface HTMLEditorUiBinder extends UiBinder<Widget, HTMLEditor> {
	}

	@UiField HtmlEditor senchaEditor; 
	
	public HTMLEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HTMLEditor(String html) {
		initWidget(uiBinder.createAndBindUi(this));
		setText(html);
		senchaEditor.setHeight("400px");
	}

	public void setText(String text) {
		senchaEditor.setValue(text);
	}

	public String getText() {
		return senchaEditor.getValue();
	}

	public String getValue() {
		return senchaEditor.getValue();
	}

	public void setValue(String html) {
		setText(html);
	}
	
	public void setHeight(int height){
		setHeight(height+"px");
		senchaEditor.setHeight(height);
	}

}
