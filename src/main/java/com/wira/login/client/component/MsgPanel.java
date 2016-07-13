package com.wira.login.client.component;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class MsgPanel extends Widget {

	private final Element ulRoot;
	private final Element spanTitle;
	private final DivElement container;

	public MsgPanel() {

		container = Document.get().createDivElement();

		this.ulRoot = DOM.createElement("ul");
		this.spanTitle = DOM.createSpan();

		//this.spanTitle.setInnerText("Summary");

		container.appendChild(this.spanTitle);
		container.appendChild(ulRoot);

		setElement(container);

		addStyleName("hide");
		//setStyleName("alert alert-danger vld-Summary");
		ulRoot.addClassName("alert alert-danger");
	}

	@Override
	public void setTitle(String title) {
		this.spanTitle.setInnerHTML(title);
	}

	public void addError(String error) {
		removeStyleName("hide");
		ulRoot.addClassName("alert-danger");
		ulRoot.removeClassName("alert-success");
		addText(error);
	}

	private void addText(String text) {
		Element liElement = DOM.createElement("li");
		liElement.setInnerHTML(text);
		this.ulRoot.appendChild(liElement);
		this.addStyleDependentName("show");
	}

	public void clear() {
		addStyleName("hide");
		this.ulRoot.setInnerHTML("");
		this.removeStyleDependentName("show");
	}

	public void addMessage(String message) {
		removeStyleName("hide");
		ulRoot.removeClassName("alert-danger");
		ulRoot.addClassName("alert-success");
		addText(message);
	}
}
