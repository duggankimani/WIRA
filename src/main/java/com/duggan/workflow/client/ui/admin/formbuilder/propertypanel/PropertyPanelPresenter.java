package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PropertyPanelPresenter extends
		PresenterWidget<PropertyPanelPresenter.MyView> {


	public interface MyView extends View {
		void showProperties(ArrayList<Property> properties, FormModel model);
		FocusPanel getPopoverFocus();
		HTMLPanel getiArrow() ;
		void showBody(boolean status, Widget w);
	}
	
	FormModel parentModel; //

	
	@Inject
	public PropertyPanelPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void save(){
		fireEvent(new SavePropertiesEvent(parentModel));
	}

	public void setProperties(FormModel parentModel, ArrayList<Property> properties) {
		this.parentModel = parentModel;
		getView().showProperties(properties, parentModel);
	}
	
}
