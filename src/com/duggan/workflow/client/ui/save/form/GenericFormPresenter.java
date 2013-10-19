package com.duggan.workflow.client.ui.save.form;

import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class GenericFormPresenter extends
		PresenterWidget<GenericFormPresenter.ICreateDocView> {

	public interface ICreateDocView extends PopupView {
		HasClickHandlers getSave();

		HasClickHandlers getCancel();

		HasClickHandlers getForward();

		Document getDocument();

		boolean isValid();

		void setForm(Form form);
				
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> UPLOAD_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	DispatchAsync requestHelper;

	private Long Id;

	@Inject
	PlaceManager placeManager;

	@Inject
	public GenericFormPresenter(final EventBus eventBus, final ICreateDocView view) {
		super(eventBus, view);
	}
	
	@Override
	protected void onBind() {
		super.onBind();

		getView().getCancel().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().hide();
			}
		});
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadForm(14L);
	}
	
	protected void loadForm(Long id) {
		GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL, id, true);
		
		requestHelper.execute(request, new TaskServiceCallback<GetFormModelResponse>() {
			@Override
			public void processResult(GetFormModelResponse result) {
				Form form = (Form)result.getFormModel().get(0);
				getView().setForm(form);
				
				getView().center();
			}
		});
	}

}
