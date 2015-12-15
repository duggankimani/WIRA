package com.duggan.workflow.client.ui.admin.datatable;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.formbuilder.upload.ImportView;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.events.EditCatalogSchemaEvent;
import com.duggan.workflow.client.ui.grid.AggregationGrid;
import com.duggan.workflow.client.ui.grid.ColumnConfig;
import com.duggan.workflow.client.ui.grid.DataMapper;
import com.duggan.workflow.client.ui.grid.DataModel;
import com.duggan.workflow.client.ui.util.StringUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Listable;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.duggan.workflow.shared.model.catalog.CatalogType;
import com.duggan.workflow.shared.model.catalog.FieldSource;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CreateTableView extends Composite {

	private static CreateTableViewUiBinder uiBinder = GWT
			.create(CreateTableViewUiBinder.class);

	interface CreateTableViewUiBinder extends UiBinder<Widget, CreateTableView> {
	}

	@UiField
	TextField txtName;
	@UiField
	TextArea txtDescription;
	@UiField
	AggregationGrid grid;
	@UiField
	HasClickHandlers aNew;
	@UiField
	IssuesPanel issues;
	@UiField
	SpanElement spnWarning;
	@UiField
	HasClickHandlers aImportCols;

	@UiField
	DropDownList<Field> lstFields;

	@UiField
	DropDownList<ProcessDef> lstProcess;

	@UiField
	DropDownList<FieldSource> lstFieldSources;

	@UiField
	Anchor aAddFields;

	@UiField
	DivElement divProcess;
	@UiField
	DivElement divFieldSource;
	@UiField
	DivElement divGrid;
	@UiField
	DivElement divAddFields;

	DataMapper mapper = new DataMapper() {

		@Override
		public List<DataModel> getDataModels(List objs) {
			List<DataModel> models = new ArrayList<DataModel>();
			for (Object o : objs) {
				CatalogColumn c = (CatalogColumn) o;
				DataModel model = new DataModel();
				model.setId(c.getId());
				model.set("fieldAutoInc", c.isAutoIncrement());
				model.set("fieldLabel", c.getLabel());
				model.set("fieldName", c.getName());
				model.set("fieldNullable", c.isNullable());
				model.set("fieldPrimary", c.isPrimaryKey());
				model.set("fieldSize", c.getSize());
				model.set("fieldType", c.getType());
				models.add(model);
			}
			return models;
		}

		@Override
		public CatalogColumn getData(DataModel model) {
			CatalogColumn col = new CatalogColumn();
			col.setId(model.getId());
			col.setAutoIncrement(model.get("fieldAutoInc") == null ? false
					: (Boolean) model.get("fieldAutoInc"));
			col.setLabel((String) model.get("fieldLabel"));
			col.setName((String) model.get("fieldName"));
			col.setNullable(model.get("fieldNullable") == null ? false
					: (Boolean) model.get("fieldNullable"));
			col.setPrimaryKey((Boolean) model.get("fieldPrimary"));
			col.setSize((Integer) model.get("fieldSize"));
			col.setType((DBType) model.get("fieldType"));

			if (isNullOrEmpty(col.getName()) || isNullOrEmpty(col.getLabel())
					|| col.getType() == null) {
				return null;
			}
			return col;
		}
	};

	private Long id;
	private CatalogType type = CatalogType.DATATABLE;
	private List<Form> forms;
	private List<ProcessDef> processes;
	private Catalog catalog;

	public CreateTableView() {
		initWidget(uiBinder.createAndBindUi(this));
		lstFields.setMultiple(true);
		lstFieldSources.setItems(Arrays.asList(FieldSource.FORM,
				FieldSource.GRID));

		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		columnConfigs.add(new ColumnConfig("fieldName", "Field Name",
				DataType.STRING, "", "input-medium"));
		columnConfigs.add(new ColumnConfig("fieldLabel", "Field Label",
				DataType.STRING, "", "input-medium"));

		ColumnConfig config = new ColumnConfig("fieldType", "Type",
				DataType.SELECTBASIC, "", "input-medium");
		config.setDropDownItems(DBType.getValues());
		columnConfigs.add(config);

		columnConfigs.add(new ColumnConfig("fieldSize", "Size",
				DataType.INTEGER, "", "input-createtable-colsize"));
		columnConfigs.add(new ColumnConfig("fieldNullable", "Null",
				DataType.BOOLEAN));
		columnConfigs.add(new ColumnConfig("fieldPrimary", "PK",
				DataType.BOOLEAN));
		columnConfigs.add(new ColumnConfig("fieldAutoInc", "Auto Incr",
				DataType.BOOLEAN));
		grid.setColumnConfigs(columnConfigs);

		aNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.addRowData(new DataModel());
			}
		});

		aAddFields.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				generateCols(lstFields.getValues());
			}
		});

		aImportCols.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final StringBuffer uploadedItems = new StringBuffer();
				UploadContext context = new UploadContext();
				context.setAction(UPLOADACTION.IMPORTGRIDDATA);
				context.setAccept(Arrays.asList("csv"));
				final String message = "This will import data from a CSV file into the grid.<br/>"
						+ "Expected Values [ColumnName,DataType,Size]";
				final ImportView view = new ImportView(message, context);
				view.setAvoidRepeatFiles(false);
				AppManager.showPopUp("Import Columns", view,
						new OptionControl() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Next")) {
									view.setVisible(false);
									Catalog catalog = getCatalog();
									catalog.setColumns(setLines(uploadedItems
											.toString()));
									EditCatalogSchemaEvent event = new EditCatalogSchemaEvent(
											catalog, false);
									AppContext.fireEvent(event);
								} else {
									view.cancelImport();
									EditCatalogSchemaEvent event = new EditCatalogSchemaEvent(
											getCatalog(), false);
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

		lstProcess.addValueChangeHandler(new ValueChangeHandler<ProcessDef>() {

			@Override
			public void onValueChange(ValueChangeEvent<ProcessDef> event) {
				if (event.getValue() == null) {
					lstFields.setItems(null);
					return;
				}
				grid.setData(new ArrayList<DataModel>());
				loadFields(event.getValue().getId());
			}
		});

		lstFieldSources
				.addValueChangeHandler(new ValueChangeHandler<FieldSource>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<FieldSource> event) {
						grid.setData(new ArrayList<DataModel>());
						loadFields(forms);
					}
				});
	}

	protected void loadFields(Long processDefId) {
		AppContext.getDispatcher().execute(
				new GetFormsRequest(processDefId, true),
				new TaskServiceCallback<GetFormsResponse>() {
					@Override
					public void processResult(GetFormsResponse aResponse) {
						loadFields(aResponse.getForms());
					}
				});
	}

	protected void generateCols(List<Field> values) {
		// Get Current Columns
		List<CatalogColumn> columns = grid.getData(mapper);

		// reset grid
		grid.setData(new ArrayList<DataModel>());
		if (values == null || values.isEmpty()) {
			return;
		}

		for (Field field : values) {
			CatalogColumn col = generateCol(field);
			if (!columns.contains(col)) {
				columns.add(col);
			}

		}

		grid.setData(mapper.getDataModels(columns));
	}

	private CatalogColumn generateCol(Field field) {
		CatalogColumn col = new CatalogColumn();
		col.setPrimaryKey(false);
		col.setAutoIncrement(false);
		col.setId(null);
		col.setNullable(false);
		String name = field.getName().toLowerCase();
		name = StringUtils.toAphanumeric(name);
		col.setName(name);
		col.setLabel(field.getCaption());
		col.setType(field.getType().toDBType());
		// Col Size
		// col.setSize(Integer.parseInt(value));

		return col;
	}

	private void loadFields(List<Form> forms) {
		this.forms = forms;
		if (forms == null)
			return;

		FieldSource source = lstFieldSources.getValue();
		if (source == null) {
			source = FieldSource.FORM;
		}

		List<Field> fields = new ArrayList<Field>();
		for (Form form : forms) {
			if (form.getFields() != null)
				for (Field field : form.getFields()) {
					if (!fields.contains(field)
							&& field.getType() != DataType.JS) {
						if (source == FieldSource.FORM
								&& field.getType() != DataType.GRID) {
							addField(fields, field);

						} else if (source == FieldSource.GRID) {
							// Grid
							for (Field child : field.getFields()) {
								addField(fields, child);
							}
						}
					}
				}
		}

		if (source == FieldSource.FORM) {
			Collections.sort(fields);
		}
		lstFields.clear();
		lstFields.setItems(fields);
	}

	private void addField(List<Field> fields, Field field) {
		Field clone = new Field() {
			public String getDisplayName() {
				return getName();
			};
		};

		field.copyTo(clone, true);
		fields.add(clone);
	}

	private List<CatalogColumn> setLines(String uploadedCSVItems) {
		if (uploadedCSVItems == null || uploadedCSVItems.trim().isEmpty()) {
			return new ArrayList<CatalogColumn>();
		}

		List<CatalogColumn> lines = new ArrayList<CatalogColumn>();

		String[] items = uploadedCSVItems.split("\n");
		for (String item : items) {
			String[] line = item.split(",");
			lines.add(createLine(line));
		}

		return lines;
	}

	public void setData(List<CatalogColumn> lines) {
		List<DataModel> models = mapper.getDataModels(lines);
		grid.setData(models);
	}

	private CatalogColumn createLine(String[] lineValues) {
		CatalogColumn col = new CatalogColumn();
		col.setPrimaryKey(false);
		col.setAutoIncrement(false);
		col.setId(null);
		col.setNullable(false);
		col.setSize(null);
		for (int i = 0; i < lineValues.length; i++) {
			String value = lineValues[i];
			if (value != null && !value.isEmpty()) {
				// remove quotes, and further trim to take care of values like
				// "" 24"
				value = value.replaceAll("\"", "");
				value = value.trim();
			}

			if (i == 0) {
				col.setName(value);
			} else if (i == 1) {
				col.setLabel(value);
			} else if (i == 2) {
				try {
					col.setType(DBType.valueOf(value));
				} catch (Exception e) {
				}
			} else if (i == 3) {
				try {
					// Col Size
					col.setSize(Integer.parseInt(value));
				} catch (Exception e) {
				}

			}

		}

		return col;
	}

	public CreateTableView(CatalogType type, Catalog catalog) {
		this();
		this.type = type;
		if (catalog != null) {
			setCatalog(catalog);
			id = catalog.getId();
		}

		if (type == CatalogType.REPORTTABLE) {
			divAddFields.removeClassName("hide");
			divFieldSource.removeClassName("hide");
			divGrid.removeClassName("hide");
			divProcess.removeClassName("hide");
		}

	}

	protected boolean isNullOrEmpty(String name) {
		return name == null || name.trim().isEmpty();
	}

	public Catalog getCatalog() {
		Catalog catalog = new Catalog();
		catalog.setId(id);
		catalog.setProcessDefId(lstProcess.getValue().getId());
		catalog.setType(type);
		catalog.setFieldSource(lstFieldSources.getValue());
		String name = txtName.getValue().toUpperCase();
		catalog.setName(name.replaceAll("\\s", "")); // Clear empty space
		catalog.setDescription(txtDescription.getValue());
		List<CatalogColumn> columns = grid.getData(mapper);

		catalog.setColumns(columns);
		return catalog;
	}

	public void setCatalog(Catalog catalog) {

		this.catalog = catalog;
		if (catalog.getRecordCount() > 0) {
			spnWarning.addClassName("label label-warning");
			spnWarning
					.setInnerText("Editing this table will lose all your existing data. Consider exporting your data first.");
		}

		txtName.setValue(catalog.getName());
		txtDescription.setValue(catalog.getDescription());
		lstFieldSources
				.setValue(catalog.getFieldSource() == null ? FieldSource.FORM
						: catalog.getFieldSource());

		grid.setData(mapper.getDataModels(catalog.getColumns()));
		List<CatalogColumn> cols = grid.getData(mapper);
	}

	public boolean isValid() {
		issues.clear();
		boolean isValid = true;

		if (type == CatalogType.REPORTTABLE && lstProcess.getValue() == null) {
			issues.addError("Please select a process");
			isValid = false;
		}

		if (txtName.getValue().trim().isEmpty()) {
			issues.addError("Table Name is mandatory");
			isValid = false;
		}

		if (getCatalog().getColumns().isEmpty()) {
			issues.addError("Cannot create table with zero columns. "
					+ "Ensure you specify Name, Label and Data Type for each column");
			isValid = false;
		}

		if (!isValid) {
			issues.getElement().scrollIntoView();
		}

		return isValid;
	}

	public void setProcesses(List<ProcessDef> processes) {
		this.processes = processes;
		lstProcess.setItems(processes);
		if (catalog != null && catalog.getProcessDefId() != null && processes!=null) {
			for (ProcessDef d : processes) {
				if (d.getId().equals(catalog.getProcessDefId())) {
					lstProcess.setValue(d);
					loadFields(d.getId());
				}
			}
		}
	}

}
