package com.duggan.workflow.client.ui.admin.users.orgs;

import com.duggan.workflow.shared.model.Organization;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class OrgsPresenter extends PresenterWidget<OrgsPresenter.MyView> {
	public interface MyView extends View {
		void setValues(String code, String name);

		HasClickHandlers getEdit();

		HasClickHandlers getDelete();
	}

	Organization organization;
	@Inject
	DispatchAsync requestHelper;

	@Inject
	public OrgsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
		getView().setValues(organization.getName(), organization.getFullName());
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

}