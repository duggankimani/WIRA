package com.duggan.workflow.client.ui.admin.datatable;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.grid.AggregationGrid;
import com.duggan.workflow.client.ui.grid.ColumnConfig;
import com.duggan.workflow.client.ui.grid.DataMapper;
import com.duggan.workflow.client.ui.grid.DataModel;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CreateDataView extends Composite {

	private static CreateDataViewUiBinder uiBinder = GWT
			.create(CreateDataViewUiBinder.class);

	interface CreateDataViewUiBinder extends UiBinder<Widget, CreateDataView> {
	}

	DataMapper mapper = new DataMapper() {

		@Override
		public List<DataModel> getDataModels(List objs) {
			List<DataModel> models = new ArrayList<DataModel>();
			for (Object o : objs) {
				DocumentLine c = (DocumentLine) o;
				DataModel model = new DataModel();
				model.setId(c.getId());

				for (Value val : c.getValues().values()) {
					model.set(val.getKey(), val.getValue());
				}
				models.add(model);
			}
			return models;
		}

		@Override
		public DocumentLine getData(DataModel model) {
			DocumentLine col = new DocumentLine();
			col.setId(model.getId());
			List<CatalogColumn> configs = catalog.getColumns();
			for (CatalogColumn config : configs) {

				Object val = model.get(config.getName());
				if(val==null){
					continue;
				}
				Value value = ColumnConfig.getValue(null, config.getName(),
						val, config.getType().getFieldType());
				col.addValue(config.getName(), value);
			}

			return col;
		}
	};
	private Catalog catalog;

	@UiField
	AggregationGrid grid;
	@UiField
	HasClickHandlers aNew;

	public CreateDataView() {
		initWidget(uiBinder.createAndBindUi(this));
		aNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.addRowData(new DataModel());
			}
		});
	}

	public CreateDataView(Catalog catalog) {
		this();
		this.catalog = catalog;
		setCatalog(catalog);
	}

	private void setCatalog(Catalog catalog) {
		List<CatalogColumn> cols = catalog.getColumns();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		for (CatalogColumn c : cols) {
			ColumnConfig config = new ColumnConfig(c.getName(), c.getLabel(), c
					.getType().getFieldType(), "", "input-medium", !c.isAutoIncrement());
			configs.add(config);
		}

		grid.setColumnConfigs(configs);
	}

	protected boolean isNullOrEmpty(String name) {
		return name == null || name.trim().isEmpty();
	}

	public void setData(List<DocumentLine> lines) {
		grid.setData(mapper.getDataModels(lines));
	}

	public List<DocumentLine> getData() {
		List<DocumentLine> lines = grid.getData(mapper);
		
		return lines;
	}

}
