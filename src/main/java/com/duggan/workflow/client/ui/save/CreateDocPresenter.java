package com.duggan.workflow.client.ui.save;

import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.events.AfterSaveEvent;
import com.duggan.workflow.shared.events.ProcessingCompletedEvent;
import com.duggan.workflow.shared.events.ProcessingEvent;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.Priority;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class CreateDocPresenter extends
		PresenterWidget<CreateDocPresenter.ICreateDocView> {

	public interface ICreateDocView extends PopupView {
		HasClickHandlers getSave();

		HasClickHandlers getCancel();

		HasClickHandlers getForward();

		Document getDocument();

		boolean isValid();

		void setDocTypes(List<DocumentType> types);

		void setValues(DocumentType docType, String subject, Date docDate,
				String partner, String value, String description,
				Priority priority, String docRefId);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> UPLOAD_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	DispatchAsync requestHelper;

	private String docRefId;

	@Inject
	PlaceManager placeManager;

	@Inject
	public CreateDocPresenter(final EventBus eventBus, final ICreateDocView view) {
		super(eventBus, view);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetDocumentTypesRequest());

		if (docRefId != null)
			requests.addRequest(new GetDocumentRequest(docRefId,null));

		requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {

					public void processResult(MultiRequestActionResult responses) {

						GetDocumentTypesResponse response = (GetDocumentTypesResponse) responses
								.get(0);
						getView().setDocTypes(response.getDocumentTypes());

						if (docRefId != null)
							showDocument((GetDocumentResult) responses.get(1));
					}
				});
	}

	protected void showDocument(GetDocumentResult result) {
		Document document = (Document)result.getDoc();

		DocumentType docType = document.getType();
		String subject = document.getCaseNo();
		Date docDate = document.getDocumentDate();
		String partner = document.getPartner();
		String value = document.getValue();
		String description = document.getDescription();
		Integer priority = document.getPriority();

		getView().setValues(docType, subject, docDate, partner, value,
				description, Priority.get(priority), document.getRefId());
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

		getView().getForward().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				Document document = getView().getDocument();
				document.setStatus(DocStatus.DRAFTED);
				document.setRefId(docRefId);
				if (getView().isValid()) {
					fireEvent(new ProcessingEvent());
					requestHelper.execute(
							new ApprovalRequest(AppContext.getUserId(),
									document),
							new TaskServiceCallback<ApprovalRequestResult>() {
								@Override
								public void processResult(
										ApprovalRequestResult result) {

									fireEvent(new ProcessingCompletedEvent());
									getView().hide();
									fireEvent(new AfterSaveEvent());
								}
							});
				}

			}
		});

		getView().getSave().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Document document = getView().getDocument();
				document.setStatus(DocStatus.DRAFTED);
				document.setRefId(docRefId);

				// document.setDescription(null);
				if (getView().isValid()) {
					requestHelper.execute(new CreateDocumentRequest(document),
							new TaskServiceCallback<CreateDocumentResult>() {
								@Override
								public void processResult(
										CreateDocumentResult result) {

									Document saved = result.getDocument();
									assert saved.getId() != null;
									fireEvent(new AfterSaveEvent());
									getView().hide();
								}
							});
				}
			}
		});
	}

	public void setDocRefId(String selectedValue) {
		this.docRefId = selectedValue;
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		ENV.clear();
	}
}
