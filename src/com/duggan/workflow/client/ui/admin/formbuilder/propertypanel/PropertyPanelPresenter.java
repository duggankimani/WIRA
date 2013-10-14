package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import java.util.List;

import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class PropertyPanelPresenter extends
		PresenterWidget<PropertyPanelPresenter.MyView> {


	public interface MyView extends PopupView {
		void showProperties(List<Property> properties);
		PopupPanel getPopUpContainer();
		FocusPanel getPopoverFocus();
		HTMLPanel getiArrow() ;
	}
	
	long fieldId=0L;
	
	@Inject
	public PropertyPanelPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public void setProperties(long id, List<Property> properties) {
		this.fieldId = id;
		getView().showProperties(properties);
	}
}
