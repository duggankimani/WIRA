package com.duggan.workflow.client.ui.admin.users.orgs;

import com.duggan.workflow.shared.model.Organization;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class OrgsPresenter extends PresenterWidget<OrgsPresenter.MyView> {
	interface MyView extends View {
	}

	Organization organization;
	@Inject
	DispatchAsync requestHelper;

	@Inject
	OrgsPresenter(EventBus eventBus, MyView view) {
		super(eventBus, view);

	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}