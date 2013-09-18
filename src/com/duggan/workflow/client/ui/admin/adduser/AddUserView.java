package com.duggan.workflow.client.ui.admin.adduser;

import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter.TYPE;
import com.gwtplatform.mvp.client.PopupViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class AddUserView extends PopupViewImpl implements
		AddUserPresenter.MyView {

	private final Widget widget;
	@UiField HTMLPanel divUserDetails;
	@UiField HTMLPanel divGroupDetails;
	@UiField Anchor aClose;

	public interface Binder extends UiBinder<Widget, AddUserView> {
	}

	@Inject
	public AddUserView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		aClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setType(TYPE type) {
		if(type==TYPE.GROUP){
			divGroupDetails.removeStyleName("hide");
			divUserDetails.addStyleName("hide");
		}else{
			divUserDetails.removeStyleName("hide");
			divGroupDetails.addStyleName("hide");
		}
	}
}
