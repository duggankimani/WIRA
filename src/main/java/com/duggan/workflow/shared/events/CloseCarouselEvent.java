package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CloseCarouselEvent extends
		GwtEvent<CloseCarouselEvent.CloseCarouselHandler> {

	public static Type<CloseCarouselHandler> TYPE = new Type<CloseCarouselHandler>();

	public interface CloseCarouselHandler extends EventHandler {
		void onCloseCarousel(CloseCarouselEvent event);
	}

	public CloseCarouselEvent() {
	}

	@Override
	protected void dispatch(CloseCarouselHandler handler) {
		handler.onCloseCarousel(this);
	}

	@Override
	public Type<CloseCarouselHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CloseCarouselHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CloseCarouselEvent());
	}
}
