package com.duggan.workflow.client.ui.admin.adduser;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;

public class AddUserPresenter extends PresenterWidget<AddUserPresenter.MyView> {

	public interface MyView extends PopupView {

		void setType(TYPE type);
	}

	public enum TYPE{
		GROUP, USER
	}
	
	TYPE type;
	@Inject
	public AddUserPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void setType(TYPE type){
		this.type = type;
		getView().setType(type);
	}
}
