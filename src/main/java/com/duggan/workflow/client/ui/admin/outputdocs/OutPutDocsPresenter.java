package com.duggan.workflow.client.ui.admin.outputdocs;

import java.util.List;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.event.CheckboxSelectionEvent.CheckboxSelectionHandler;
import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.PopupType;
import com.duggan.workflow.client.ui.admin.outputdocs.save.SaveOutPutDocsPresenter;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.component.HTMLEditor;
import com.duggan.workflow.client.ui.events.EditOutputDocEvent;
import com.duggan.workflow.client.ui.events.EditOutputDocEvent.EditOutputDocHandler;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.requests.GetOutputDocumentRequest;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveOutputDocumentRequest;
import com.duggan.workflow.shared.responses.GetOutputDocumentResponse;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveOutputDocumentResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class OutPutDocsPresenter extends
		Presenter<OutPutDocsPresenter.MyView, OutPutDocsPresenter.MyProxy>
		implements EditOutputDocHandler, SearchHandler,
		CheckboxSelectionHandler {

	public interface MyView extends View {
		HasClickHandlers getDocumentButton();

		HasClickHandlers getEdit();

		HasClickHandlers getEditDoc();

		HasClickHandlers getDelete();

		void setOutputDocuments(List<OutputDocument> documents);

		void setModelSelected(boolean value);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.outputdocs)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<OutPutDocsPresenter> {
	}

	@Inject
	SaveOutPutDocsPresenter saveProvider;

	@Inject
	DispatchAsync requestHelper;

	private String processRefId;

	private Object selectedModel;

	@Inject
	public OutPutDocsPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy, BaseProcessPresenter.CONTENT_SLOT);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditOutputDocEvent.TYPE, this);
		addRegisteredHandler(SearchEvent.getType(), this);
		addRegisteredHandler(CheckboxSelectionEvent.getType(), this);

		getView().getDocumentButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(null);
			}
		});

		getView().getDelete().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final OutputDocument doc = (OutputDocument) selectedModel;
				AppManager.showPopUp("Delete '" + doc.getName() + "'",
						"Do you want to delete this document?",
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Yes")) {
									save(doc);
								}
							}
						}, "Yes", "Cancel");
			}
		});

		getView().getEdit().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final OutputDocument doc = (OutputDocument) selectedModel;
				showEditPopup(doc);
			}
		});

		getView().getEditDoc().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final OutputDocument doc = (OutputDocument) selectedModel;
				GetOutputDocumentRequest request = new GetOutputDocumentRequest(
						doc.getRefId(),true);
				
				requestHelper.execute(request,
						new TaskServiceCallback<GetOutputDocumentResponse>() {
							@Override
							public void processResult(
									GetOutputDocumentResponse aResponse) {
								String html = aResponse.getDocument()
										.getTemplate();
								final HTMLEditor editor = new HTMLEditor(html);
								int height = Window.getClientHeight()-250;
								editor.setHeight(height);
								
								AppManager.showPopUp("Edit " + doc.getName()
										+ "", editor, new OnOptionSelected() {

									@Override
									public void onSelect(String name) {
										if (name.equals("Yes")) {
											save(doc);
										}
									}
								}, PopupType.FULLPAGE, "Yes", "Cancel");

							}
						});

			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		fireEvent(new ProcessChildLoadedEvent(this));
		processRefId = request.getParameter("p", null);
		load();
	}

	protected void showEditPopup(OutputDocument doc) {
		saveProvider.clear();
		saveProvider.setOutputDoc(doc);
		AppManager.showPopUp("Create Output Document", saveProvider.asWidget(),
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							OutputDocument doc = saveProvider
									.getOutputDocument();
							save(doc);
						}
						hide();
					}

				}, "Save", "Cancel");

	}

	protected void load() {
		load(null);
	}

	protected void load(String searchTerm) {
		GetOutputDocumentsRequest request = new GetOutputDocumentsRequest(
				processRefId);
		request.setSearchTerm(searchTerm);
		requestHelper.execute(request,
				new TaskServiceCallback<GetOutputDocumentsResponse>() {
					@Override
					public void processResult(
							GetOutputDocumentsResponse aResponse) {
						getView().setModelSelected(false);
						getView().setOutputDocuments(aResponse.getDocuments());
					}
				});
	}

	private void save(OutputDocument doc) {
		doc.setProcessRefId(processRefId);
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new SaveOutputDocumentRequest(doc));
		requests.addRequest(new GetOutputDocumentsRequest(processRefId));
		requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResult) {
						SaveOutputDocumentResponse aSaveResponse = (SaveOutputDocumentResponse) aResult
								.get(0);

						GetOutputDocumentsResponse aGetOutputDocsResult = (GetOutputDocumentsResponse) aResult
								.get(1);
						getView().setOutputDocuments(
								aGetOutputDocsResult.getDocuments());
					}
				});
	}

	@Override
	public void onEditOutputDoc(EditOutputDocEvent event) {
		final OutputDocument doc = event.getDoc();
		if (!doc.isActive()) {
			// deleting
			AppManager.showPopUp("Delete '" + doc.getName() + "'",
					"Do you want to delete this document?",
					new OnOptionSelected() {

						@Override
						public void onSelect(String name) {
							if (name.equals("Yes")) {
								save(doc);
							}
						}
					}, "Yes", "Cancel");

		} else {
			showEditPopup(doc);
		}
	}

	@Override
	public void onSearch(SearchEvent event) {
		if (isVisible()) {
			load(event.getFilter().getPhrase());
		}
	}

	@Override
	public void onCheckboxSelection(CheckboxSelectionEvent event) {

		selectedModel = event.getModel();
		selectItem(selectedModel, event.getValue());

		if (!event.getValue()) {
			selectedModel = null;
		}
	}

	private void selectItem(Object model, boolean value) {
		getView().setModelSelected(value);
	}

}
