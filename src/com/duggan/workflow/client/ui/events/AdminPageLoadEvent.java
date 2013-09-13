package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class AdminPageLoadEvent extends
		GwtEvent<AdminPageLoadEvent.AdminPageLoadHandler> {

	public static Type<AdminPageLoadHandler> TYPE = new Type<AdminPageLoadHandler>();

	public interface AdminPageLoadHandler extends EventHandler {
		void onAdminPageLoad(AdminPageLoadEvent event);
	}

	private Boolean isAdminPage;
	
	public AdminPageLoadEvent(boolean isAdminPage) {
		this.isAdminPage = isAdminPage;
	}

	@Override
	protected void dispatch(AdminPageLoadHandler handler) {
		handler.onAdminPageLoad(this);
	}

	public boolean isAdminPage(){
		return isAdminPage;
	}
	
	@Override
	public Type<AdminPageLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AdminPageLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Boolean isAdminPage) {
		source.fireEvent(new AdminPageLoadEvent(isAdminPage));
	}
}
