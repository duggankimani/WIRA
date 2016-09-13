package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.text.client.DoubleParser;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValueBox;

public class DoubleField extends ValueBox<Double> {

	private DoubleRenderer doubleRenderer;

	public DoubleField() {
		this(Document.get().createTextInputElement(),
				DoubleField.DoubleRenderer.instance(), DoubleParser.instance());
	}

	private DoubleField(Element element, Renderer<Double> renderer,
			Parser<Double> parser) {
		super(element, renderer, parser);
		doubleRenderer = (DoubleRenderer)renderer;
	}

	public static DoubleField wrap(Element numberEl, boolean ishtmlFormElement) {
		// Assert that the element is attached.
		assert Document.get().getBody().isOrHasChild(numberEl);

		numberEl.setAttribute("type", "text");
		DoubleField numberBox = new DoubleField(numberEl,
				DoubleField.DoubleRenderer.instance(), DoubleParser.instance());

		// Mark it attached and remember it for cleanup.
		numberBox.onAttach();
		if (!ishtmlFormElement) {
			RootPanel.detachOnWindowClose(numberBox);
		}
		//

		return numberBox;
	}

	public void setPlaceholder(String placeHolderValue) {
		getElement().setAttribute("placeHolder", placeHolderValue);
	}

	public void setClass(String className) {
		setStyleName(className);
	}

	public void setType(String type) {
		getElement().setAttribute("type", type);
	}
	
	public void setFormat(String format){
		if(format.equalsIgnoreCase("Integer")){
			doubleRenderer.setFormat("#,###;(#,###)");
		}else if(format.equalsIgnoreCase("Double")){
			doubleRenderer.setFormat("#,##0.00;(#,##0.00)");
		}else{
			doubleRenderer.setFormat(format);
		}
		
	}

	static class DoubleRenderer extends AbstractRenderer<Double> {

		String format = "#,##0.00;(#,##0.00)";

		/**
		 * Returns the instance.
		 */
		public static Renderer<Double> instance() {
			return new DoubleRenderer();
		}

		protected DoubleRenderer() {
		}

		public String render(Double object) {
			if (object == null) {
				return "";
			}
			return NumberFormat.getFormat(getFormat()).format(object);
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format.trim();
		}
	}

	public String getFormat() {
		return doubleRenderer.getFormat();
	}
}
