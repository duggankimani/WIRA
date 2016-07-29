package com.duggan.workflow.client.ui.component;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.text.client.DoubleParser;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueBox;

public class DoubleField extends ValueBox<Double> {

	public DoubleField() {
		this(Document.get().createTextInputElement(), DoubleField.DoubleRenderer
				.instance(), DoubleParser.instance());
	}
	
	private DoubleField(Element element, Renderer<Double> renderer, Parser<Double> parser) {
		super(element, renderer, parser);
	}

	
	public static DoubleField wrap(Element numberEl) {
	    // Assert that the element is attached.
	    assert Document.get().getBody().isOrHasChild(numberEl);

	    numberEl.setAttribute("type", "text");
		DoubleField numberBox = new DoubleField(numberEl, DoubleField.DoubleRenderer
				.instance(), DoubleParser.instance());
		
	    // Mark it attached and remember it for cleanup.
		numberBox.onAttach();
	   // RootPanel.detachOnWindowClose(textBox);

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
	
	static class DoubleRenderer extends AbstractRenderer<Double> {
		  private static DoubleRenderer INSTANCE;

		  /**
		   * Returns the instance.
		   */
		  public static Renderer<Double> instance() {
		    if (INSTANCE == null) {
		      INSTANCE = new DoubleRenderer();
		    }
		    return INSTANCE;
		  }

		  protected DoubleRenderer() {
		  }

		  public String render(Double object) {
		    if (object == null) {
		      return "";
		    }
		    return NumberFormat.getFormat("#,##0;(#,##0)").format(object);
		    //return NumberFormat.getDecimalFormat().format(object);
		  }
		}
}
