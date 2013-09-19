package com.duggan.workflow.client.ui.admin.adduser;

import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter.TYPE;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class AddUserView extends PopupViewImpl implements
		AddUserPresenter.MyView {

	private final Widget widget;
	@UiField HTMLPanel divUserDetails;
	@UiField HTMLPanel divGroupDetails;
	@UiField Anchor aClose;
	@UiField PopupPanel AddUserDialog;
	@UiField FocusPanel sltContainer;
	@UiField Element sltBox;
	@UiField Element sltDrop;
	@UiField Anchor aSelectedCancel;
	@UiField Element ulSelectResults;
	@UiField Anchor liSelectItem;
	

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
		
		sltContainer.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				sltDrop.removeClassName("hidden");
				sltBox.addClassName("select2-dropdown-open");
			}
		});
		
		sltContainer.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//sltDrop.addClassName("hidden");
			}
		});
		
		liSelectItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//ulSelectResults.
			}
		});
		
		//AddUserDialog.setPopupPosition(40,10);
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
