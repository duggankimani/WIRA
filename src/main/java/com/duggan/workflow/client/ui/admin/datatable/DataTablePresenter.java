package com.duggan.workflow.client.ui.admin.datatable;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.events.EditCatalogEvent;
import com.duggan.workflow.client.ui.events.EditCatalogEvent.EditCatalogHandler;
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
import com.duggan.workflow.shared.requests.DeleteCatalogRequest;
import com.duggan.workflow.shared.requests.GetCatalogsRequest;
import com.duggan.workflow.shared.requests.GetDataRequest;
import com.duggan.workflow.shared.requests.InsertDataRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveCatalogRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;
import com.duggan.workflow.shared.responses.GetDataResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveCatalogResponse;

public class DataTablePresenter
		extends
		Presenter<DataTablePresenter.IDataTableView, DataTablePresenter.IDataTableProxy>
		implements EditCatalogHandler {

	public interface IDataTableView extends View {
		HasClickHandlers getNewButton();

		HasClickHandlers getImportButton();

		void bindCatalogs(List<Catalog> catalogs);
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
		addRegisteredHandler(EditCatalogEvent.TYPE, this);
		getView().getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPopup(null);
			}
		});

	}

	protected void showPopup(Catalog catalog) {
		final CreateTableView view = new CreateTableView(catalog);
		AppManager.showPopUp(catalog.getId()!=null?
				"Edit Table":"Create Table", view, "create-data-table-popup",
				new OptionControl() {

					@Override
					public void onSelect(String name) {

						if (name.equals("Save")) {
							if (view.isValid()) {
								Catalog cat = view.getCatalog();
								save(cat);

								hide();
							}
						} else {
							hide();
						}
					}

				}, "Save", "Cancel");
	}

	private void showDataPopup(final Catalog catalog) {
		final CreateDataView view = new CreateDataView(catalog);
		requestHelper.execute(new GetDataRequest(catalog.getId()),
				new ServiceCallback<GetDataResponse>() {
					@Override
					public void processResult(GetDataResponse aResponse) {
						view.setData(aResponse.getLines());

						AppManager.showPopUp("Data View", view,
								"create-data-table-popup", new OptionControl() {

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
					}
				});

	}
	
	private void saveData(Catalog catalog,
			List<DocumentLine> data) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new InsertDataRequest(catalog, data));
		action.addRequest(new GetCatalogsRequest());
		requestHelper.execute(action,  new ServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				
				List<Catalog> catalogs = ((GetCatalogsResponse) aResponse
						.get(1)).getCatalogs();
				getView().bindCatalogs(catalogs);
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
				new ServiceCallback<GetCatalogsResponse>() {
					@Override
					public void processResult(GetCatalogsResponse aResponse) {
						List<Catalog> catalogs = aResponse.getCatalogs();
						getView().bindCatalogs(catalogs);
					}
				});
	}

	public void save(Catalog catalog) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveCatalogRequest(catalog));
		action.addRequest(new GetCatalogsRequest());

		requestHelper.execute(action,
				new ServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {

						Catalog saved = ((SaveCatalogResponse) aResponse.get(0))
								.getCatalog();

						List<Catalog> catalogs = ((GetCatalogsResponse) aResponse
								.get(1)).getCatalogs();
						getView().bindCatalogs(catalogs);
					}
				});
	}

	@Override
	public void onEditCatalog(EditCatalogEvent event) {
		if (event.isEditData()) {
			showDataPopup(event.getCatalog());
		} else if (event.isDelete()) {
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
				new ServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {

						BaseResponse r = aResponse.get(0);

						List<Catalog> catalogs = ((GetCatalogsResponse) aResponse
								.get(1)).getCatalogs();
						getView().bindCatalogs(catalogs);
					}
				});
	}
}