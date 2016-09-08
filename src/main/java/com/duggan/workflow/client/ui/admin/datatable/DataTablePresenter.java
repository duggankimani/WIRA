package com.duggan.workflow.client.ui.admin.datatable;

import java.util.ArrayList;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.event.CheckboxSelectionEvent.CheckboxSelectionHandler;
import com.duggan.workflow.client.place.NameTokens;
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
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogType;
import com.duggan.workflow.shared.requests.DeleteCatalogRequest;
import com.duggan.workflow.shared.requests.GetCatalogsRequest;
import com.duggan.workflow.shared.requests.GetDataRequest;
import com.duggan.workflow.shared.requests.GetProcessCategoriesRequest;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.InsertDataRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveCatalogRequest;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;
import com.duggan.workflow.shared.responses.GetDataResponse;
import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveCatalogResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.duggan.workflow.client.service.TaskServiceCallback;

public class DataTablePresenter
		extends
		Presenter<DataTablePresenter.IDataTableView, DataTablePresenter.IDataTableProxy>
		implements EditCatalogDataHandler, EditCatalogSchemaHandler,
		SearchHandler, CheckboxSelectionHandler {

	public interface IDataTableView extends View {
		HasClickHandlers getNewButton();

		HasClickHandlers getImportButton();

		void bindCatalogs(ArrayList<Catalog> catalogs);

		HasClickHandlers getNewReportLink();

		HasClickHandlers getNewReportViewLink();

		void setSelected(boolean isSelected);

		HasClickHandlers getViewDataLink();

		HasClickHandlers getEditLink();

		HasClickHandlers getDeleteLink();
	}

	public static final String DATATABLES_CAN_VIEW_DATATABLES = "DATATABLES_CAN_VIEW_DATATABLES";
	
	@ProxyCodeSplit
	@NameToken(NameTokens.datatable)
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({DATATABLES_CAN_VIEW_DATATABLES})
	public interface IDataTableProxy extends
			TabContentProxyPlace<DataTablePresenter> {
	}

	@TabInfo(container = AdminHomePresenter.class)
	static TabData getTabLabel(HasPermissionsGateKeeper gateKeeper) {
		/**
		 * Manually calling gateKeeper.withParams Method.
		 * 
		 * HACK NECESSITATED BY THE FACT THAT Gin injects to different instances of this GateKeeper in 
		 * Presenter.MyProxy->UseGateKeeper & 
		 * getTabLabel(GateKeeper);
		 * 
		 * Test -> 
		 * Window.alert in GateKeeper.canReveal(this+" Params = "+params) Vs 
		 * Window.alert here in getTabLabel.canReveal(this+" Params = "+params) Vs
		 * Window.alert in AbstractTabPanel.refreshTabs(tab.getTabData.getGateKeeper()+" Params = "+params) Vs
		 * 
		 */
		gateKeeper.withParams(new String[]{DATATABLES_CAN_VIEW_DATATABLES}); 
		TabDataExt ext = new TabDataExt(TABLABEL, "glyphicon glyphicon-th", 8, gateKeeper);
		return ext;
	}

	public static final Object TABLE_SLOT = new Object();

	public static final String TABLABEL = "Data Tables";

	@Inject
	DispatchAsync requestHelper;

	private Object selectedModel;

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
		addRegisteredHandler(SearchEvent.getType(), this);
		addRegisteredHandler(CheckboxSelectionEvent.getType(), this);

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

		getView().getNewReportViewLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPopup(CatalogType.REPORTVIEW, null);
			}
		});

		getView().getEditLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Catalog cat = ((Catalog) selectedModel);
				showPopup(cat);
			}
		});

		getView().getViewDataLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Catalog cat = ((Catalog) selectedModel);
				showDataPopup(cat);
			}
		});

		getView().getDeleteLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteCatalog((Catalog) selectedModel);
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

	private void showPopup(final CatalogType type, Catalog catalog) {
		final CreateTableView view = new CreateTableView(type, catalog);

		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProcessCategoriesRequest());

		if (type == CatalogType.REPORTTABLE) {
			action.addRequest(new GetProcessesRequest(false));
		} else if (type == CatalogType.REPORTVIEW) {
			action.addRequest(new GetCatalogsRequest(true));
		}

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						GetProcessCategoriesResponse getCategories = (GetProcessCategoriesResponse) aResponse
								.get(0);
						view.setCategories(getCategories.getCategories());

						if (type == CatalogType.REPORTTABLE) {
							GetProcessesResponse getProcesses = (GetProcessesResponse) aResponse
									.get(1);
							view.setProcesses(getProcesses.getProcesses());
						} else if (type == CatalogType.REPORTVIEW) {
							GetCatalogsResponse getCatalogs = (GetCatalogsResponse) aResponse
									.get(1);
							view.setViews(getCatalogs.getCatalogs());
						}
					}
				});

		AppManager.showPopUp(
				(catalog == null || catalog.getId() == null) ? "Create Table"
						: "Edit Table", view, "create-data-table-popup",
				new OptionControl() {

					@Override
					public void onSelect(String name) {

						if (name.equals("Save")) {
							if (view.isValid()) {
								Catalog cat = view.getCatalog();
								save(this, cat);
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

	private void showDataPopup(final Catalog catalog, ArrayList<DocumentLine> lines) {
		final CreateDataView view = new CreateDataView(catalog);

		String[] actions = new String[] { "Save", "Cancel" };
		if (catalog.getType() == CatalogType.REPORTVIEW) {
			actions = new String[] { "Close" };
		}

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

				}, actions);

		view.setData(lines);
	}

	private void saveData(Catalog catalog, ArrayList<DocumentLine> data) {
		fireEvent(new ProcessingEvent());
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new InsertDataRequest(catalog.getId(), data));
		action.addRequest(new GetCatalogsRequest());
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {

						ArrayList<Catalog> catalogs = ((GetCatalogsResponse) aResponse
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
		loadData(null);
	}

	private void loadData(String searchTerm) {
		fireEvent(new ProcessingEvent("Loading..."));
		GetCatalogsRequest getCats = new GetCatalogsRequest();
		getCats.setSearchTerm(searchTerm);

		requestHelper.execute(getCats,
				new TaskServiceCallback<GetCatalogsResponse>() {
					@Override
					public void processResult(GetCatalogsResponse aResponse) {
						selectItem(null, false);
						fireEvent(new ProcessingCompletedEvent());
						ArrayList<Catalog> catalogs = aResponse.getCatalogs();
						getView().bindCatalogs(catalogs);
					}
				});
	}

	public void save(final OptionControl ctrl, Catalog catalog) {
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

						ArrayList<Catalog> catalogs = ((GetCatalogsResponse) aResponse
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

	private void deleteCatalog(final Catalog catalog) {
		
		AppManager.showPopUp("Confirm Delete", "Delete catalog '"+catalog.getDescription()+"'", new OptionControl() {
			
			@Override
			public void onSelect(String name) {
				
				if(name.equals("Delete")){
					MultiRequestAction action = new MultiRequestAction();
					action.addRequest(new DeleteCatalogRequest(catalog));
					action.addRequest(new GetCatalogsRequest());

					requestHelper.execute(action,
							new TaskServiceCallback<MultiRequestActionResult>() {
								@Override
								public void processResult(MultiRequestActionResult aResponse) {

									ArrayList<Catalog> catalogs = ((GetCatalogsResponse) aResponse
											.get(1)).getCatalogs();
									getView().bindCatalogs(catalogs);
									hide();
								}
							});
				}else{
					hide();
				}
				
			}
		}, "Cancel","Delete");
			}

	@Override
	public void onSearch(SearchEvent event) {
		if (isVisible()) {
			loadData(event.getFilter().getPhrase());
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

	private void selectItem(Object model, boolean isSelected) {
		getView().setSelected(isSelected);
	}
}