package com.duggan.workflow.client.ui.admin.datatable;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.events.EditCatalogDataEvent;
import com.duggan.workflow.client.ui.events.EditCatalogDataEvent.EditCatalogDataHandler;
import com.duggan.workflow.client.ui.events.EditCatalogSchemaEvent;
import com.duggan.workflow.client.ui.events.EditCatalogSchemaEvent.EditCatalogSchemaHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogType;
import com.duggan.workflow.shared.requests.DeleteCatalogRequest;
import com.duggan.workflow.shared.requests.GetCatalogsRequest;
import com.duggan.workflow.shared.requests.GetDataRequest;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.InsertDataRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveCatalogRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;
import com.duggan.workflow.shared.responses.GetDataResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveCatalogResponse;

public class DataTablePresenter
		extends
		Presenter<DataTablePresenter.IDataTableView, DataTablePresenter.IDataTableProxy>
		implements EditCatalogDataHandler, EditCatalogSchemaHandler {

	public interface IDataTableView extends View {
		HasClickHandlers getNewButton();

		HasClickHandlers getImportButton();

		void bindCatalogs(List<Catalog> catalogs);

		HasClickHandlers getNewReportLink();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.datatable)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface IDataTableProxy extends
			TabContentProxyPlace<DataTablePresenter> {
	}

	@TabInfo(container = AdminHomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt ext = new TabDataExt("Data Tables", "icon-th", 8,
				adminGatekeeper);
		return ext;
	}

	public static final Object TABLE_SLOT = new Object();

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public DataTablePresenter(final EventBus eventBus,
			final IDataTableView view, IDataTableProxy proxy) {
		super(eventBus, view, proxy, AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditCatalogDataEvent.TYPE, this);
		addRegisteredHandler(EditCatalogSchemaEvent.TYPE, this);
		getView().getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPopup(null);
			}
		});

		getView().getNewReportLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPopup(CatalogType.REPORTTABLE, null);
			}
		});

	}

	protected void showPopup(Catalog catalog) {
		if (catalog == null || catalog.getType() == null) {
			showPopup(CatalogType.DATATABLE, catalog);
		} else {
			showPopup(catalog.getType(), catalog);
		}
	}

	private void showPopup(CatalogType type, Catalog catalog) {
		final CreateTableView view = new CreateTableView(type,catalog);
		if(type==CatalogType.REPORTTABLE){
			GetProcessesRequest request = new GetProcessesRequest();
			requestHelper.execute(request, new TaskServiceCallback<GetProcessesResponse>() {
				@Override
				public void processResult(GetProcessesResponse aResponse) {
					view.setProcesses(aResponse.getProcesses());
				}
			});
		}
		AppManager.showPopUp(
				(catalog == null || catalog.getId() == null) ? "Create Table"
						: "Edit Table", view, "create-data-table-popup",
				new OptionControl() {

					@Override
					public void onSelect(String name) {

						if (name.equals("Save")) {
							if (view.isValid()) {
								Catalog cat = view.getCatalog();
								save(this,cat);
							}
						} else {
							hide();
						}
					}

				}, "Save", "Cancel");
	}

	private void showDataPopup(final Catalog catalog) {
		fireEvent(new ProcessingEvent());
		requestHelper.execute(new GetDataRequest(catalog.getId()),
				new TaskServiceCallback<GetDataResponse>() {
					@Override
					public void processResult(GetDataResponse aResponse) {
						showDataPopup(catalog, aResponse.getLines());
						fireEvent(new ProcessingCompletedEvent());
					}
				});

	}

	private void showDataPopup(final Catalog catalog, List<DocumentLine> lines) {
		final CreateDataView view = new CreateDataView(catalog);
		AppManager.showPopUp("Data View", view, "create-data-table-popup",
				new OptionControl() {

					@Override
					public void onSelect(String name) {

						if (name.equals("Save")) {
							saveData(catalog, view.getData());
							hide();
						} else {
							hide();
						}
					}

				}, "Save", "Cancel");

		view.setData(lines);
	}

	private void saveData(Catalog catalog, List<DocumentLine> data) {
		fireEvent(new ProcessingEvent());
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new InsertDataRequest(catalog.getId(), data));
		action.addRequest(new GetCatalogsRequest());
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {

						List<Catalog> catalogs = ((GetCatalogsResponse) aResponse
								.get(1)).getCatalogs();
						getView().bindCatalogs(catalogs);
						fireEvent(new ProcessingCompletedEvent());
					}
					
					@Override
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		loadData();
	}

	private void loadData() {
		requestHelper.execute(new GetCatalogsRequest(),
				new TaskServiceCallback<GetCatalogsResponse>() {
					@Override
					public void processResult(GetCatalogsResponse aResponse) {
						List<Catalog> catalogs = aResponse.getCatalogs();
						getView().bindCatalogs(catalogs);
					}
				});
	}

	public void save(final OptionControl ctrl,Catalog catalog) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveCatalogRequest(catalog));
		action.addRequest(new GetCatalogsRequest());

		fireEvent(new ProcessingEvent());
		
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {

						Catalog saved = ((SaveCatalogResponse) aResponse.get(0))
								.getCatalog();

						List<Catalog> catalogs = ((GetCatalogsResponse) aResponse
								.get(1)).getCatalogs();
						getView().bindCatalogs(catalogs);
						ctrl.hide();
						fireEvent(new ProcessingCompletedEvent());
					}
					
				});
	}

	@Override
	public void onEditCatalogData(EditCatalogDataEvent event) {
		if (event.isEditData()) {
			if (event.getLines() != null) {
				showDataPopup(event.getCatalog(), event.getLines());
			} else {
				showDataPopup(event.getCatalog());
			}

		} else if (event.isDelete()) {
			deleteCatalog(event.getCatalog());
		} else {
			showPopup(event.getCatalog());
		}
	}

	@Override
	public void onEditCatalogSchema(EditCatalogSchemaEvent event) {
		if (event.isDelete()) {
			deleteCatalog(event.getCatalog());
		} else {
			showPopup(event.getCatalog());
		}
	}

	private void deleteCatalog(Catalog catalog) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new DeleteCatalogRequest(catalog));
		action.addRequest(new GetCatalogsRequest());

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {

						BaseResponse r = aResponse.get(0);// what to do?

						List<Catalog> catalogs = ((GetCatalogsResponse) aResponse
								.get(1)).getCatalogs();
						getView().bindCatalogs(catalogs);
					}
				});
	}

}