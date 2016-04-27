package com.duggan.workflow.client.reports;

import java.util.List;

import javax.inject.Inject;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class ReportsView extends ViewImpl implements ReportsPresenter.IReportsView {
	interface Binder extends UiBinder<Widget, ReportsView> {
	}

	@UiField
	FlexTable tableReports;
	
	@UiField
	Element divReportName;
	
	@UiField
	Element divReportView;

	@Inject
	public ReportsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void bind(Catalog catalog, List<DocumentLine> data) {
		divReportView.removeClassName("hide");
		tableReports.clear();
		tableReports.removeAllRows();
		tableReports.addStyleName("table-striped");
		divReportName.setInnerText(""+catalog.getDescription());

		int col = 0;
		int i = 0;
		for (CatalogColumn catCol : catalog.getColumns()) {

			tableReports.setWidget(i, col, new InlineLabel(catCol.getLabel()));
			tableReports.getFlexCellFormatter().addStyleName(i, col,
					"headers_style");
			++col;
		}

		for (DocumentLine line : data) {
			++i;
			col = 0;
			for (CatalogColumn catCol : catalog.getColumns()) {
				Value value = line.getValue(catCol.getName());
				String val = value == null ? "" : value.getValue() == null ? ""
						: value.getValue() + "";
				tableReports.setWidget(i, col, new InlineLabel(val));
				tableReports.getFlexCellFormatter().addStyleName(i, col,
						"data_style");
				++col;
			}
		}
	}

	@Override
	public void bindCatalogs(List<Catalog> catalogs) {
		divReportView.addClassName("hide");
		tableReports.clear();
		tableReports.removeAllRows();
		tableReports.removeStyleName("table-striped");

		ProcessCategory category = null;

		int col = 0;
		int row1 = 0;
		int row2 = 0;
		int i = 0;
		for (Catalog catalog : catalogs) {

			// Set default category if null
			if (catalog.getCategory() == null) {
				ProcessCategory cat = new ProcessCategory();
				cat.setName("General");
				catalog.setCategory(cat);
			}

			if (category == null) {
				i = row1;
				category = catalog.getCategory();

				HTMLPanel header = new HTMLPanel("" + category.getDisplayName());
				// header.addStyleName("header");
				tableReports.setWidget(i, col + 1, header);
				tableReports.getFlexCellFormatter().setWidth(i, col, "50px");
				tableReports.getFlexCellFormatter().addStyleName(i, col + 1,
						"permissions");

				++i;
			} else if (!category.equals(catalog.getCategory())) {
				if (col == 0) {
					col = 3;
					i = row2;
				} else {
					col = 0;
					i = row1;
				}
				category = catalog.getCategory();
				HTMLPanel header = new HTMLPanel("" + category.getDisplayName());
				// header.addStyleName("header");
				tableReports.getFlexCellFormatter().setWidth(i, 0, "50px");
				tableReports.setWidget(i, col + 1, header);
				tableReports.getFlexCellFormatter().addStyleName(i, col + 1,
						"permissions");

				++i;
			}

			tableReports.setWidget(i, col, new CheckBox());
			String desc = catalog.getDescription() + " ("
					+ catalog.getRecordCount() + ")";
			ActionLink link = new ActionLink(desc);
			HTMLPanel description = new HTMLPanel("");
			description.add(link);
			link.setHref("#reports;reportRefId=" + catalog.getRefId());
			description.addStyleName("item");
			tableReports.setWidget(i, col + 1, description);
			tableReports.getFlexCellFormatter().addStyleName(i, col + 1,
					"permissions");

			++i;

			if (col == 0) {
				row1 = i;
			} else {
				row2 = i;
			}
		}
	}

}