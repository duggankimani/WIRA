package com.duggan.workflow.client.ui.component;

import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;

public class ActionLink extends Anchor {

	private Object model;

	public ActionLink() {

		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// setStyleName("hidden");
			}
		});
	}

	public ActionLink(Element element) {
		super(element);
	}

	public ActionLink(Object model) {
		this.model = model;
		if (model instanceof String) {
			setText(model.toString());
		}
	}

	public void setDataToggle(String data) {
		getElement().setAttribute("data-toggle", data);
	}

	public void setDataTarget(String data) {
		getElement().setAttribute("data-target", data);
	}

	public void setRole(String role) {
		getElement().setAttribute("role", role);
	}

	public void setDataOriginalTitle(String data) {
		getElement().setAttribute("data-original-title", data);
	}

	public void setDataPlacement(String data) {
		getElement().setAttribute("data-placement", data);
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONDBLCLICK:
		case Event.ONFOCUS:
		case Event.ONCLICK:
			if (!isEnabled()) {
				return;
			}
			break;
		}
		super.onBrowserEvent(event);
	}

	public void setLoadingState(boolean isLoading) {
		if (isLoading) {
			addStyleName("disabled");
			getElement().setAttribute("disabled", "disabled");
			getElement().getStyle().setProperty("pointerEvents", "none");
		} else {
			removeStyleName("disabled");
			getElement().removeAttribute("disabled");
			getElement().getStyle().setProperty("pointerEvents", "auto");
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		setLoadingState(!enabled);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		AppContext.getEventBus().addHandler(ProcessingEvent.TYPE,
				new ProcessingHandler() {
					@Override
					public void onProcessing(ProcessingEvent event) {
						setEnabled(false);
					}
				});

		AppContext.getEventBus().addHandler(ProcessingCompletedEvent.TYPE,
				new ProcessingCompletedHandler() {
					@Override
					public void onProcessingCompleted(
							ProcessingCompletedEvent event) {
						setEnabled(true);
					}
				});
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public static ActionLink wrap(Element element, boolean isHTMLWrappedField) {
		// Assert that the element is attached.
		assert Document.get().getBody().isOrHasChild(element);

		ActionLink anchor = new ActionLink(element);

		// Mark it attached and remember it for cleanup.
		anchor.onAttach();

		if (!isHTMLWrappedField) {
			RootPanel.detachOnWindowClose(anchor);
		}
		return anchor;
	}
}
