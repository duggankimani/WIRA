package com.duggan.workflow.client.ui.admin.datatable;

import java.util.ArrayList;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.events.EditCatalogDataEvent;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogType;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DataTableView extends ViewImpl implements
		DataTablePresenter.IDataTableView {

	private final Widget widget;
	@UiField
	Anchor aNew;
	@UiField
	TableView tblView;
	
	@UiField
	Anchor aImportTable;
	@UiField
	Anchor aImportTab;
	@UiField
	Anchor aImportComma;
	
	@UiField
	Anchor aExport;
	@UiField
	Anchor aExportTable;
	@UiField
	Anchor aExportData;
	
	@UiField
	Anchor aNewReport;
	@UiField
	Anchor aNewReportView;

	@UiField
	FlexTable tblDataTable;

	@UiField
	Anchor aViewData;
	@UiField
	Anchor aEdit;
	@UiField
	Anchor aDelete;

	@UiField
	Element divTableActions;
	@UiField
	Element divGeneralActions;
	
	@UiField Uploader aUploader;
	private Object selected;

	public interface Binder extends UiBinder<Widget, DataTableView> {
	}

	@Inject
	public DataTableView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		aUploader.getElement().setId("csvuploader");
		aUploader.setAvoidRepeatFiles(false);
		final UploadContext ctx = new UploadContext();
		final ArrayList<String> types = new ArrayList<String>();
		types.add("csv");
		
		ctx.setAccept(types);
		
		aImportTable.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				/**
				 * CSV Formats
				 * DEFAULT
				 * EXCEL - comma separated
				 * TDF - Tab separated
				 */
				ctx.setAction(UPLOADACTION.IMPORTTABLE);
				types.clear();
				types.add("json");
				ctx.setAccept(types);
				aUploader.setContext(ctx);
				triggerUpload(aUploader.getElement());
			}
		});
		
		aImportTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				/**
				 * CSV Formats
				 * DEFAULT
				 * EXCEL - comma separated
				 * TDF - Tab separated
				 */
				ctx.setAction(UPLOADACTION.IMPORTCSV);
				types.clear();
				types.add("csv");
				ctx.setAccept(types);
				ctx.setContext("f", "TDF");
				aUploader.setContext(ctx);
				triggerUpload(aUploader.getElement());
			}
		});
		
		aImportComma.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				/**
				 * CSV Formats
				 * DEFAULT
				 * EXCEL - comma separated
				 * TDF - Tab separated
				 */
				ctx.setAction(UPLOADACTION.IMPORTCSV);
				types.clear();
				types.add("csv");
				ctx.setAccept(types);
				ctx.setContext("f", "RFC4180");
				aUploader.setContext(ctx);
				triggerUpload(aUploader.getElement());
			}
		});
		
		
		aExport.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(selected==null){
					return;
				}
				
				Catalog cat = (Catalog)selected;
				Window.open("getreport?action=exportdatatable&catalogRefId="+cat.getRefId(), "_blank", null);
			}
		});
		
		aExportData.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(selected==null){
					return;
				}
				
				Catalog cat = (Catalog)selected;
				Window.open("getreport?action=exportdatatable&filter=dataonly&catalogRefId="+cat.getRefId(), "_blank", null);
			}
		});
		
		aExportTable.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(selected==null){
					return;
				}
				
				Catalog cat = (Catalog)selected;
				Window.open("getreport?action=exportdatatable&filter=tableonly&catalogRefId="+cat.getRefId(), "_blank", null);
			}
		});
	}

	protected native void triggerUpload(Element uploader) /*-{
		$wnd.jQuery(uploader).find('input').trigger('click');
		
	}-*/;

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getNewButton() {
		return aNew;
	}

	@Override
	public void bindCatalogs(ArrayList<Catalog> catalogs) {
		clearSelections();
		tblDataTable.removeAllRows();
		createHeaders(tblDataTable);

		int i = 1;
		for (Catalog catalog : catalogs) {
			int j = 0;
			Checkbox box = new Checkbox(catalog);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			tblDataTable.setWidget(i, j++, box);
			tblDataTable.setWidget(i, j++,
					new HTMLPanel(catalog.getDescription()));
			tblDataTable.setWidget(i, j++, new HTMLPanel(catalog.getName()));
			tblDataTable.setWidget(i, j++,
					new HTMLPanel(catalog.getRecordCount() + ""));
			tblDataTable.setWidget(
					i,
					j++,
					new HTMLPanel(
							catalog.getType() == null ? CatalogType.DATATABLE
									.getDisplayName() : catalog.getType()
									.getDisplayName()));
			++i;
		}
	}

	private void clearSelections() {

	}

	private void createHeaders(FlexTable table) {
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Description</strong>"));
		table.setWidget(0, j++, new HTMLPanel("<strong>Name</strong>"));
		table.setWidget(0, j++, new HTMLPanel("<strong>Records</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Type</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");

		for (int i = 0; i < table.getCellCount(0); i++) {
			table.getFlexCellFormatter().setStyleName(0, i, "th");
		}
	}

	private HTMLPanel getActions(Catalog c) {
		HTMLPanel panel = new HTMLPanel("");
		panel.addStyleName("action-commands");

		final Anka data = new Anka("Data", c);
		data.addStyleName("btn btn-primary");
		data.getElement().setInnerHTML("<i class=\"icon-hdd\"></i>");
		panel.add(data);
		data.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditCatalogDataEvent(
						data.getCatalog(), true, true));
			}
		});

		final Anka edit = new Anka("", c);
		edit.addStyleName("btn btn-success");
		edit.getElement().setInnerHTML(
				"<i class=\"icon-pencil helper-14\"></i>");
		edit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditCatalogDataEvent(edit.getCatalog()));
			}
		});
		panel.add(edit);

		final Anka delete = new Anka("", c);
		delete.addStyleName("btn btn-danger");
		delete.getElement().setInnerHTML("<i class=\"icon-trash\"></i>");
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Delete",
						"Do you want to delete Table '"
								+ delete.getCatalog().getName() + "'",
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Yes")) {
									AppContext
											.fireEvent(new EditCatalogDataEvent(
													delete.getCatalog(), true));
								}
							}
						}, "Yes", "Cancel");
			}
		});
		panel.add(delete);

		return panel;
	}

	class Anka extends ActionLink {
		Catalog catalog;

		public Anka(String text, Catalog c) {
			super(text);
			this.catalog = c;
		}

		public Catalog getCatalog() {
			return catalog;
		}
	}

	@Override
	public HasClickHandlers getImportButton() {
		return aImportTab;
	}

	@Override
	public HasClickHandlers getNewReportLink() {
		return aNewReport;
	}

	@Override
	public HasClickHandlers getNewReportViewLink() {
		return aNewReportView;
	}
	
	@Override
	public HasClickHandlers getDeleteLink() {
		return aDelete;
	}
	
	@Override
	public HasClickHandlers getEditLink() {
		return aEdit;
	}
	
	@Override
	public HasClickHandlers getViewDataLink() {
		return aViewData;
	}

	@Override
	public void setSelected(Object model, boolean isSelected) {
		divGeneralActions.removeClassName("hide");
		divTableActions.removeClassName("hide");

		this.selected = model;
		
		if (isSelected) {
			divGeneralActions.addClassName("hide");
		} else {
			this.selected = null;
			divTableActions.addClassName("hide");
		}
	}
	
	public Uploader getUploader(){
		return aUploader;
	}
}
