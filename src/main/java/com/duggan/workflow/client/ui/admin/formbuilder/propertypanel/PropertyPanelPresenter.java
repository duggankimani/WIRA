package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import java.util.ArrayList;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.duggan.workflow.shared.requests.GetDSConfigurationsRequest;
import com.duggan.workflow.shared.responses.GetDSConfigurationsResponse;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.DSConfiguration;

public class PropertyPanelPresenter extends
		PresenterWidget<PropertyPanelPresenter.MyView> {


	public interface MyView extends View {
		void showProperties(ArrayList<Property> properties, FormModel model, ArrayList<KeyValuePair> datasources);
		FocusPanel getPopoverFocus();
		HTMLPanel getiArrow() ;
		void showBody(boolean status, Widget w);
	}
	
	FormModel parentModel; //
	private ArrayList<Property> properties;

	@Inject DispatchAsync dispatcher;
	
	@Inject
	public PropertyPanelPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void save(){
		if(parentModel instanceof Field){
			((Field)parentModel).setProperties(properties);
		}else{
			((Form)parentModel).setProperties(properties);
		}
		fireEvent(new SavePropertiesEvent(parentModel));
	}
	
	public void setProperties(FormModel aParentModel, ArrayList<Property> aPropertyList) {
		this.parentModel = aParentModel;
		this.properties = aPropertyList;

		dispatcher.execute(new GetDSConfigurationsRequest(),
				new TaskServiceCallback<GetDSConfigurationsResponse>(){
			@Override
			public void processResult(
					GetDSConfigurationsResponse aResponse) {
				ArrayList<KeyValuePair> datasources = new ArrayList<>(); 
				for(DSConfiguration config: aResponse.getConfigurations()){
					KeyValuePair pair = new KeyValuePair(config.getName(),config.getName());
					datasources.add(pair);
				}
				getView().showProperties(properties, parentModel,datasources);
			}
		});
	}
	
}
