package com.duggan.workflow.client.ui.admin.datatable;

import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.events.EditCatalogDataEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
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
	Anchor aImport;


	public interface Binder extends UiBinder<Widget, DataTableView> {
	}

	@Inject
	public DataTableView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		tblView.setHeaders(Arrays.asList("Name", "Description", "Records",
				"Last Modified", "Actions"));
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getNewButton() {
		return aNew;
	}

	@Override
	public void bindCatalogs(List<Catalog> catalogs) {
		tblView.clearRows();
		for (Catalog c : catalogs) {
			tblView.addRow(new InlineLabel(c.getName()),
					new InlineLabel(c.getDescription()),
					new InlineLabel(c.getRecordCount()+""), new InlineLabel(""),
					(HTMLPanel) getActions(c));
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
				AppContext.fireEvent(new EditCatalogDataEvent(data.getCatalog(), true,true));
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
									AppContext.fireEvent(new EditCatalogDataEvent(
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
		return aImport;
	}

}
