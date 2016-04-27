package com.duggan.workflow.client.ui.admin.outputdocs;

import java.util.List;

import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.outputdocs.save.SaveOutPutDocsPresenter;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.events.EditOutputDocEvent;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.EditOutputDocEvent.EditOutputDocHandler;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveOutputDocumentRequest;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveOutputDocumentResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
		implements EditOutputDocHandler, SearchHandler {

	public interface MyView extends View {
		HasClickHandlers getDocumentButton();

		void setOutputDocuments(List<OutputDocument> documents);
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
		getView().getDocumentButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(null);
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
		GetOutputDocumentsRequest request = new GetOutputDocumentsRequest(processRefId);
		request.setSearchTerm(searchTerm);
		requestHelper.execute(request,
				new TaskServiceCallback<GetOutputDocumentsResponse>() {
					@Override
					public void processResult(
							GetOutputDocumentsResponse aResponse) {
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
		if(isVisible()){
			load(event.getFilter().getPhrase());
		}
	}
}
