package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class IssuesPanel extends Widget {

	private final Element ulRoot;
	private final Element spanTitle;
	private final DivElement container;

	public IssuesPanel() {

		container = Document.get().createDivElement();

		this.ulRoot = DOM.createElement("ul");
		this.spanTitle = DOM.createSpan();

		//this.spanTitle.setInnerText("Summary");

		container.appendChild(this.spanTitle);
		container.appendChild(ulRoot);

		setElement(container);

		//setStyleName("alert alert-danger vld-Summary");
		setStyleName("vld-Summary");
	}

	@Override
	public void setTitle(String title) {
		this.spanTitle.setInnerHTML(title);
	}

	public void addError(String error) {

		Element liElement = DOM.createElement("li");
		liElement.setInnerHTML(error);
		this.ulRoot.appendChild(liElement);

		this.addStyleDependentName("show");
	}

	public void clear() {
		this.ulRoot.setInnerHTML("");
		this.removeStyleDependentName("show");
	}
}
