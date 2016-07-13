package com.wira.login.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;

public class LoginRootPresenter extends RootPresenter {

	public static final class LoginView extends RootView {
        @Override
        public void setInSlot(Object slot, IsWidget widget) {
            RootPanel.get("mainContent").add(widget);
        }
    }

    @Inject
    LoginRootPresenter(EventBus eventBus, LoginView rootView) {
        super( eventBus, rootView );
    }
}
