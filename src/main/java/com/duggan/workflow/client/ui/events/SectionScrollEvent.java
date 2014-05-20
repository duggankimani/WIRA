package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SectionScrollEvent extends
		GwtEvent<SectionScrollEvent.SectionScrollHandler> {

	public static Type<SectionScrollHandler> TYPE = new Type<SectionScrollHandler>();
	private String sectionName;

	public interface SectionScrollHandler extends EventHandler {
		void onSectionScroll(SectionScrollEvent event);
	}

	public SectionScrollEvent(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionName() {
		return sectionName;
	}

	@Override
	protected void dispatch(SectionScrollHandler handler) {
		handler.onSectionScroll(this);
	}

	@Override
	public Type<SectionScrollHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SectionScrollHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String sectionName) {
		source.fireEvent(new SectionScrollEvent(sectionName));
	}
}
