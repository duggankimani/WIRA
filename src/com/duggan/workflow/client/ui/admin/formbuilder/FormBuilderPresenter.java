package com.duggan.workflow.client.ui.admin.formbuilder;


import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent.PropertyChangedHandler;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.ui.events.SaveFormDesignEvent;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.client.ui.events.SaveFormDesignEvent.SaveFormDesignHandler;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent.SavePropertiesHandler;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.CreateFormRequest;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.responses.CreateFormResponse;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class FormBuilderPresenter extends
		PresenterWidget<FormBuilderPresenter.IFormBuilderView> implements SavePropertiesHandler,
		PropertyChangedHandler, SaveFormDesignHandler{

	public interface IFormBuilderView extends View {
		Anchor getNewButton();
		
		InlineLabel getFormLabel();

		void setForm(Form form);
		
		Form getForm();

		void setProperty(String property, String value);

		void setForms(List<Form> forms);
		
		HasValueChangeHandlers<Form> getFormDropDown();

		void clear();

	}

	@Inject DispatchAsync dispatcher;
	
	@Inject
	public FormBuilderPresenter(final EventBus eventBus, final IFormBuilderView view) {
		super(eventBus, view);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SavePropertiesEvent.TYPE, this);
		addRegisteredHandler(PropertyChangedEvent.TYPE, this);
		addRegisteredHandler(SaveFormDesignEvent.TYPE, this);
		getView().getFormDropDown().addValueChangeHandler(new ValueChangeHandler<Form>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Form> event) {
								
				Form form = event.getValue();
				
				if(form.getId()==null){
					return;
				}

				loadForm(form.getId());
				
			}
		});
		
		getView().getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().clear();
				
				Form form = getView().getForm();
				form.setCaption(null);
				form.setName(null);
				form.setId(null);
				saveForm(form);
			}
		});
		
	}

	protected void loadForm(Long id) {
		GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL, id, true);
		
		dispatcher.execute(request, new TaskServiceCallback<GetFormModelResponse>() {
			@Override
			public void processResult(GetFormModelResponse result) {
				Form form = (Form)result.getFormModel().get(0);
				getView().setForm(form);
			}
		});
	}

	public void loadFormList(){
		dispatcher.execute(new GetFormsRequest(), new TaskServiceCallback<GetFormsResponse>() {
			@Override
			public void processResult(GetFormsResponse result) {
				getView().setForms(result.getForms());
			}
		});
	}

	protected void saveForm(Form form) {
		
		dispatcher.execute(new CreateFormRequest(form), new TaskServiceCallback<CreateFormResponse>() {
			@Override
			public void processResult(CreateFormResponse result) {
				getView().setForm(result.getForm());
				
				loadFormList();
			}
		});
	}
	

	@Override
	protected void onReset() {
		super.onReset();
		loadFormList();
	}

	@Override
	public void onSaveProperties(SavePropertiesEvent event) {
		if(event.getParent()!=null && event.getParent().equals(this)){
			saveForm(getView().getForm());
		}
	}

	@Override
	public void onPropertyChanged(PropertyChangedEvent event) {
		if(event.isForField())
			return;
		
		String property = event.getPropertyName();
		String value= event.getPropertyValue();
		
		getView().setProperty(property, value);
		saveForm(getView().getForm());
	}
	
	@Override
	public void onSaveFormDesign(SaveFormDesignEvent event) {
		saveForm(getView().getForm());
	}
	
}
