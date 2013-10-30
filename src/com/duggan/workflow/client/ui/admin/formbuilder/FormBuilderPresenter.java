package com.duggan.workflow.client.ui.admin.formbuilder;

import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent.PropertyChangedHandler;
import com.duggan.workflow.client.ui.events.SaveFormDesignEvent;
import com.duggan.workflow.client.ui.events.SaveFormDesignEvent.SaveFormDesignHandler;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent.SavePropertiesHandler;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.CreateFormRequest;
import com.duggan.workflow.shared.requests.DeleteFormModelRequest;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.CreateFormResponse;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class FormBuilderPresenter extends
		PresenterWidget<FormBuilderPresenter.IFormBuilderView> implements
		SavePropertiesHandler, PropertyChangedHandler, SaveFormDesignHandler {

	public interface IFormBuilderView extends View {
		Anchor getNewButton();

		Anchor getDeleteButton();

		InlineLabel getFormLabel();

		void setForm(Form form);

		Form getForm();

		void setProperty(String property, String value);

		void setForms(List<Form> forms);

		void activatePalette();

		void registerInputDrag();

		HasValueChangeHandlers<Form> getFormDropDown();

		void clear();

	}

	@Inject
	DispatchAsync dispatcher;

	@Inject
	public FormBuilderPresenter(final EventBus eventBus,
			final IFormBuilderView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SavePropertiesEvent.TYPE, this);
		addRegisteredHandler(PropertyChangedEvent.TYPE, this);
		addRegisteredHandler(SaveFormDesignEvent.TYPE, this);
		getView().getFormDropDown().addValueChangeHandler(
				new ValueChangeHandler<Form>() {

					@Override
					public void onValueChange(ValueChangeEvent<Form> event) {
						getView().registerInputDrag();
						getView().activatePalette();

						Form form = event.getValue();

						if (form.getId() == null) {
							return;
						}

						loadForm(form.getId());
					}
				});

		getView().getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().clear();
				getView().registerInputDrag();
				getView().activatePalette();

				Form form = getView().getForm();
				form.setCaption(null);
				form.setName(null);
				form.setId(null);
				saveForm(form);
			}
		});

		getView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final Form form = getView().getForm();
				AppManager.showPopUp("Confirm Delete", new HTMLPanel(
						"Do you want to delete form \"" + form.getCaption()+"\""),
						new OnOptionSelected() {

							@Override
							public void onSelect(String button) {
								if (button.equals("Ok")) {
									delete(form);
								}
							}

						}, "Cancel", "Ok");
			}
		});
	}

	private void delete(Form form) {
		MultiRequestAction action = new MultiRequestAction();
		DeleteFormModelRequest request = new DeleteFormModelRequest(form);
		action.addRequest(request);
		
		GetFormsRequest formsRequest = new GetFormsRequest();
		action.addRequest(formsRequest);
		
		dispatcher.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {
						getView().clear();
						getView().setForm(new Form());
						
						GetFormsResponse resp = (GetFormsResponse)result.get(1);
						getView().setForms(resp.getForms());
					}
				});
	}

	protected void loadForm(Long id) {
		GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL,
				id, true);

		dispatcher.execute(request,
				new TaskServiceCallback<GetFormModelResponse>() {
					@Override
					public void processResult(GetFormModelResponse result) {
						Form form = (Form) result.getFormModel().get(0);
						getView().setForm(form);
					}
				});
	}

	protected void saveForm(Form form) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new CreateFormRequest(form));
		
		GetFormsRequest formsRequest = new GetFormsRequest();
		action.addRequest(formsRequest);
		dispatcher.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {
						CreateFormResponse createResp = (CreateFormResponse)result.get(0);
						getView().setForm(createResp.getForm());
						
						GetFormsResponse response = (GetFormsResponse)result.get(1);
						getView().setForms(response.getForms());
					}
				});
	}

	@Override
	protected void onReset() {
		super.onReset();
		loadFormList();
	}
	
	private void loadFormList() {
		dispatcher.execute(new GetFormsRequest(),
				new TaskServiceCallback<GetFormsResponse>() {
					@Override
					public void processResult(GetFormsResponse result) {
						getView().setForms(result.getForms());
					}
				});
	}

	@Override
	public void onSaveProperties(SavePropertiesEvent event) {
		if (event.getParent() != null
				&& event.getParent().equals(getView().getForm())) {
			saveForm(getView().getForm());
		}
	}

	@Override
	public void onPropertyChanged(PropertyChangedEvent event) {
		if (event.isForField())
			return;

		String property = event.getPropertyName();
		String value = event.getPropertyValue().toString();

		getView().setProperty(property, value);
		saveForm(getView().getForm());
	}

	@Override
	public void onSaveFormDesign(SaveFormDesignEvent event) {
		saveForm(getView().getForm());
	}

}
