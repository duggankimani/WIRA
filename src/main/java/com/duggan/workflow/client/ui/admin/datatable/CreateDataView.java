package com.duggan.workflow.client.ui.admin.datatable;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.ArrayList;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.admin.formbuilder.upload.ImportView;
import com.duggan.workflow.client.ui.events.EditCatalogDataEvent;
import com.duggan.workflow.client.ui.grid.AggregationGrid;
import com.duggan.workflow.client.ui.grid.ColumnConfig;
import com.duggan.workflow.client.ui.grid.DataMapper;
import com.duggan.workflow.client.ui.grid.DataModel;
import com.duggan.workflow.client.ui.util.ArrayUtil;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CreateDataView extends Composite {

	private static CreateDataViewUiBinder uiBinder = GWT
			.create(CreateDataViewUiBinder.class);

	interface CreateDataViewUiBinder extends UiBinder<Widget, CreateDataView> {
	}

	DataMapper mapper = new DataMapper() {

		@Override
		public ArrayList<DataModel> getDataModels(ArrayList objs) {
			ArrayList<DataModel> models = new ArrayList<DataModel>();
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
			ArrayList<CatalogColumn> configs = catalog.getColumns();
			for (CatalogColumn config : configs) {

				Object val = model.get(config.getName());
				if (val == null) {
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

	@UiField
	HasClickHandlers aImport;

	public CreateDataView() {
		initWidget(uiBinder.createAndBindUi(this));
		aNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.addRowData(new DataModel());
			}
		});

		aImport.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final StringBuffer uploadedItems = new StringBuffer();
				UploadContext context = new UploadContext();
				context.setAction(UPLOADACTION.IMPORTGRIDDATA);
				ArrayList<String> values = ArrayUtil.asList("csv");
				context.setAccept(values);
				
				final String message = "This will import data from a CSV file into the grid.";
				final ImportView view = new ImportView(message, context);
				view.setAvoidRepeatFiles(false);
				AppManager.showPopUp("Import Data", view, new OptionControl() {

					@Override
					public void onSelect(String name) {
						if (name.equals("Next")) {
							// AppContext.fireEvent(new ReloadEvent());
							view.setVisible(false);

							setLines(uploadedItems.toString());
							EditCatalogDataEvent event = new EditCatalogDataEvent(
									catalog, false, true);
							event.setLines(getData());
							AppContext.fireEvent(event);
							// AppCon
						} else {
							view.cancelImport();
							EditCatalogDataEvent event = new EditCatalogDataEvent(
									catalog, false, true);
							AppContext.fireEvent(event);
						}
					}

				}, "Next", "Cancel");
				view.getUploader().addOnFinishUploaderHandler(
						new OnFinishUploaderHandler() {

							@Override
							public void onFinish(IUploader uploader) {
								String msg = uploader.getServerMessage()
										.getMessage();
								view.setMessage(message + "<p>" + msg + "</p>");
								uploadedItems.append(msg);
							}
						});
			}
		});
	}

	private void setLines(String uploadedCSVItems) {
		if (uploadedCSVItems == null || uploadedCSVItems.trim().isEmpty()) {
			return;
		}

		ArrayList<DocumentLine> lines = new ArrayList<DocumentLine>();

		String[] items = uploadedCSVItems.split("\n");
		for (String item : items) {
			String[] line = item.split(",");
			lines.add(createLine(line));
		}

		setData(lines);
	}

	private DocumentLine createLine(String[] lineValues) {

		DocumentLine docline = new DocumentLine();
		ArrayList<CatalogColumn> columnConfigs = catalog.getColumns();

		for (int i = 0; i < lineValues.length && i < columnConfigs.size(); i++) {

			CatalogColumn col = columnConfigs.get(i);
			Field field = col.toFormField();
//			new Field();
//			field.setCaption(col.getLabel());
//			field.setName(col.getName());
//			field.setFormId(System.currentTimeMillis());
//			field.setType(col.getType().getFieldType());

			FieldWidget widget = FieldWidget.getWidget(col.getType()
					.getFieldType(), field, false);

			Value val = widget.from(col.getName(), lineValues[i]);
			if ((val == null || val.getValue() == null || val.getValue()
					.toString().trim().isEmpty())
					&& col.getType().getFieldType().equals(DataType.DOUBLE)) {
				// Window.alert("Setting default val for: "+col.getName());
				docline.addValue(col.getName(), new DoubleValue(0.0));

			} else {
				// Window.alert("Setting val "+val.getValue()+" for: "+col.getName());
				docline.addValue(col.getName(), val);
			}

		}

		return docline;
	}

	public CreateDataView(Catalog catalog) {
		this();
		this.catalog = catalog;
		setCatalog(catalog);
	}

	private void setCatalog(Catalog catalog) {
		ArrayList<CatalogColumn> cols = catalog.getColumns();
		ArrayList<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		for (CatalogColumn c : cols) {
			ColumnConfig config = new ColumnConfig(c.getName(), c.getLabel(), c
					.getType().getFieldType(), "", "input-medium",
					!c.isAutoIncrement());
			configs.add(config);
		}

		grid.setColumnConfigs(configs);
	}

	protected boolean isNullOrEmpty(String name) {
		return name == null || name.trim().isEmpty();
	}

	public void setData(ArrayList<DocumentLine> lines) {
		grid.setData(mapper.getDataModels(lines));
	}

	public ArrayList<DocumentLine> getData() {
		ArrayList<DocumentLine> lines = new ArrayList<DocumentLine>();

		try {
			lines = grid.getData(mapper);
		} catch (Exception e) {
			Window.alert("Error: "+e.getMessage());
			GWT.log(e.getMessage());
			throw new RuntimeException(e);
		}
		return lines;
	}

}
