package com.duggan.workflow.client.ui.admin.datatable;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.ArrayList;
import java.util.Collections;

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
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.grid.AggregationGrid;
import com.duggan.workflow.client.ui.grid.ColumnConfig;
import com.duggan.workflow.client.ui.grid.DataMapper;
import com.duggan.workflow.client.ui.grid.DataModel;
import com.duggan.workflow.client.ui.util.StringUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.ProcessCategory;
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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.client.util.ArrayUtil;

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
	DropDownList<Catalog> lstViews;

	@UiField
	DropDownList<ProcessCategory> lstCategories;

	@UiField
	DropDownList<FieldSource> lstFieldSources;

	@UiField
	DropDownList<Field> lstGridField;

	@UiField
	Anchor aAddFields;

	@UiField
	DivElement divProcess;
	@UiField
	DivElement divViews;
	@UiField
	DivElement divFieldSource;
	@UiField
	DivElement divGridField;
	@UiField
	DivElement divGrid;
	@UiField
	DivElement divAddFields;

	DataMapper mapper = new DataMapper() {

		@Override
		public ArrayList<DataModel> getDataModels(ArrayList objs) {
			ArrayList<DataModel> models = new ArrayList<DataModel>();
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
			col.setSize((String) model.get("fieldSize"));
			col.setType((DBType) model.get("fieldType"));

			if (isNullOrEmpty(col.getName()) || isNullOrEmpty(col.getLabel())
					|| col.getType() == null) {
				return null;
			}
			return col;
		}
	};

	private CatalogType type = CatalogType.DATATABLE;
	private ArrayList<Form> forms;
	private ArrayList<ProcessDef> processes;
	private Catalog catalog;

	public CreateTableView() {
		initWidget(uiBinder.createAndBindUi(this));
		lstFields.setMultiple(true);
		ArrayList<FieldSource> values = new ArrayList<FieldSource>();
		values.addAll(ArrayUtil.asList(FieldSource.FORM, FieldSource.GRID));
		lstFieldSources.setItems(values);

		ArrayList<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		columnConfigs.add(new ColumnConfig("fieldName", "Field Name",
				DataType.STRING, "", "input-medium"));
		columnConfigs.add(new ColumnConfig("fieldLabel", "Field Label",
				DataType.STRING, "", "input-medium"));

		ColumnConfig config = new ColumnConfig("fieldType", "Type",
				DataType.SELECTBASIC, "", "input-medium");
		config.setDropDownItems(DBType.getValues());
		columnConfigs.add(config);

		columnConfigs.add(new ColumnConfig("fieldSize", "Size",
				DataType.STRING, "", "input-createtable-colsize"));
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
				ArrayList<String> values = ArrayUtil.asList("csv");
				context.setAccept(values);
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
					if (type == CatalogType.REPORTTABLE) {
						lstFields.setItems(null);
					}
					return;
				}

				if (type == CatalogType.REPORTTABLE) {
					grid.setData(new ArrayList<DataModel>());
					loadFields(event.getValue().getRefId());
				}
			}
		});

		lstFieldSources
				.addValueChangeHandler(new ValueChangeHandler<FieldSource>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<FieldSource> event) {
						grid.setData(new ArrayList<DataModel>());
						lstGridField.clear();
						if (event.getValue() == FieldSource.GRID) {
							divGridField.removeClassName("hide");
						} else {
							divGridField.addClassName("hide");
						}

						loadFields(forms);
					}
				});

		lstGridField.addValueChangeHandler(new ValueChangeHandler<Field>() {
			@Override
			public void onValueChange(ValueChangeEvent<Field> event) {
				grid.setData(new ArrayList<DataModel>());
				loadFields(event.getValue());
			}
		});

		lstViews.addValueChangeHandler(new ValueChangeHandler<Catalog>() {
			@Override
			public void onValueChange(ValueChangeEvent<Catalog> event) {
				Catalog catalog = event.getValue();
				bindView(catalog);
			}
		});
	}

	public CreateTableView(CatalogType type, Catalog catalog) {
		this();
		this.type = type;
		if (catalog != null) {
			setCatalog(catalog);
		}

		if (type == CatalogType.REPORTTABLE) {
			divAddFields.removeClassName("hide");
			divFieldSource.removeClassName("hide");
			divGrid.removeClassName("hide");
			divProcess.removeClassName("hide");
			if (catalog != null && catalog.getFieldSource() == FieldSource.GRID) {
				divGridField.removeClassName("hide");
			}
		} else if (type == CatalogType.REPORTVIEW) {
			lstProcess.addStyleName("hide");
			lstFieldSources.addStyleName("hide");
			divAddFields.removeClassName("hide");
			divGrid.removeClassName("hide");
			divViews.removeClassName("hide");
		}

	}

	protected void loadFields(final String processRefId) {

		AppContext.fireEvent(new ProcessingEvent("Loading..."));
		AppContext.getDispatcher().execute(
				new GetFormsRequest(processRefId, true),
				new TaskServiceCallback<GetFormsResponse>() {
					@Override
					public void processResult(GetFormsResponse aResponse) {
						loadFields(aResponse.getForms());
						AppContext.fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	protected void generateCols(ArrayList<Field> values) {
		// Get Current Columns
		ArrayList<CatalogColumn> columns = grid.getData(mapper);

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
		String name = field.getName();//.toLowerCase(); - Report Tables are case Sensitive
		if(type!=CatalogType.REPORTTABLE){
			//Report Tables are case Sensitive since values are mapped to form values
			name = field.getName().toLowerCase();
		}
		name = StringUtils.toAphanumeric(name);
		col.setName(name);
		col.setLabel(field.getCaption());
		col.setType(field.getType().toDBType());
		// Col Size
		// col.setSize(Integer.parseInt(value));

		return col;
	}

	private void loadFields(Field gridField) {
		// Grid
		ArrayList<Field> fields = new ArrayList<Field>();
		if (gridField != null)
			for (Field child : gridField.getFields()) {
				addField(fields, child);
			}
		Collections.sort(fields);
		lstFields.clear();
		lstFields.setItems(fields);
	}

	private void loadFields(ArrayList<Form> forms) {
		this.forms = forms;
		if (forms == null)
			return;

		FieldSource source = lstFieldSources.getValue();

		if (source == null) {
			source = FieldSource.FORM;
		}

		ArrayList<Field> fields = new ArrayList<Field>();
		ArrayList<Field> gridFields = new ArrayList<>();

		for (Form form : forms) {
			if (form.getFields() != null) {
				for (Field field : form.getFields()) {
					if (!fields.contains(field)
							&& field.getType() != DataType.JS) {
						if (source == FieldSource.FORM
								&& field.getType() != DataType.GRID) {
							addField(fields, field);

						} else if (source == FieldSource.GRID
								&& field.getType() == DataType.GRID) {
							// This is for lstGridFields Field - First loop
							gridFields.add(field);
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

		lstGridField.setItems(gridFields);
		if (catalog != null && catalog.getGridName() != null) {
			lstGridField.setValueByKey(catalog.getGridName());
			loadFields(lstGridField.getValue());
		}
	}

	private void addField(ArrayList<Field> fields, Field field) {
		Field clone = new Field() {
			public String getDisplayName() {
				return getName();
			};
		};

		if (field.getType() == DataType.FORM) {
			// HTML Forms containing multiple fields
			for (Field customFormField : field.getFields()) {
				addField(fields, customFormField);
			}
			return;
		}

		field.copyTo(clone, true);
		fields.add(clone);
	}

	private ArrayList<CatalogColumn> setLines(String uploadedCSVItems) {
		if (uploadedCSVItems == null || uploadedCSVItems.trim().isEmpty()) {
			return new ArrayList<CatalogColumn>();
		}

		ArrayList<CatalogColumn> lines = new ArrayList<CatalogColumn>();

		String[] items = uploadedCSVItems.split("\n");
		for (String item : items) {
			String[] line = item.split(",");
			lines.add(createLine(line));
		}

		return lines;
	}

	public void setData(ArrayList<CatalogColumn> lines) {
		ArrayList<DataModel> models = mapper.getDataModels(lines);
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
					col.setSize(value);
				} catch (Exception e) {
				}

			}

		}

		return col;
	}

	protected boolean isNullOrEmpty(String name) {
		return name == null || name.trim().isEmpty();
	}

	public Catalog getCatalog() {
		Catalog cat = new Catalog();
		if (this.catalog != null) {
			cat.setId(this.catalog.getId());
		}

		if (lstProcess.getValue() != null) {
			cat.setProcessDefId(lstProcess.getValue().getId());
		}

		cat.setType(type);
		cat.setGridName(lstGridField.getValue() == null ? null : lstGridField
				.getValue().getName());
		cat.setCategory(lstCategories.getValue());
		cat.setFieldSource(lstFieldSources.getValue());
		String name = txtName.getValue().toUpperCase();
		cat.setName(name.replaceAll("\\s", "")); // Clear empty space
		cat.setDescription(txtDescription.getValue());
		ArrayList<CatalogColumn> columns = grid.getData(mapper);

		cat.setColumns(columns);
		return cat;
	}

	public void setCatalog(Catalog catalog) {

		this.catalog = catalog;
		if (catalog == null) {
			return;
		}

		if (catalog.getType() != CatalogType.REPORTVIEW) {
			if (catalog.getRecordCount() > 0) {
				spnWarning.addClassName("label label-danger");
				spnWarning
						.setInnerText("Editing this table will lose all your existing data. "
								+ "Back up your data first.");
			}
		} else {
			bindFields(catalog);
		}

		txtName.setValue(catalog.getName());
		txtDescription.setValue(catalog.getDescription());
		lstFieldSources
				.setValue(catalog.getFieldSource() == null ? FieldSource.FORM
						: catalog.getFieldSource());
		grid.setData(mapper.getDataModels(catalog.getColumns()));
		ArrayList<CatalogColumn> cols = grid.getData(mapper);
	}

	protected void bindView(Catalog viewCatalog) {
		if (catalog != null) {
			if (catalog.getName().equals(viewCatalog.getName())) {
				setCatalog(viewCatalog);
			}
		} else {
			setCatalog(viewCatalog);
		}

		if (viewCatalog == null) {
			return;
		}

		bindFields(viewCatalog);
	}

	private void bindFields(Catalog viewCatalog) {
		ArrayList<Field> fields = new ArrayList<Field>();
		for (CatalogColumn col : viewCatalog.getColumns()) {
			fields.add(col.toFormField());
		}

		lstFields.setItems(fields);
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

		if (lstFieldSources.getValue() != null
				&& lstFieldSources.getValue() == FieldSource.GRID) {
			if (lstGridField.getValue() == null) {
				issues.addError("Please Select at least one Grid");
				isValid = false;
			}
		}

		if (!isValid) {
			issues.getElement().scrollIntoView();
		}

		return isValid;
	}

	public void setProcesses(ArrayList<ProcessDef> processes) {
		this.processes = processes;
		lstProcess.setItems(processes);
		if (catalog != null && catalog.getProcessDefId() != null
				&& processes != null) {
			for (ProcessDef d : processes) {
				if (d.getId().equals(catalog.getProcessDefId())) {
					setProcess(d);
					loadFields(d.getRefId());
				}
			}
		}
	}

	private void setProcess(ProcessDef process) {
		lstProcess.setValue(process);
		if (process.getCategory() != null
				&& (catalog == null || catalog.getCategory() == null)) {
			lstCategories.setValue(process.getCategory());
		}
	}

	public void setViews(ArrayList<Catalog> catalogs) {
		lstViews.setItems(catalogs);
		if (this.catalog != null) {
			lstViews.setValue(catalog);
		}
	}

	public void setCategories(ArrayList<ProcessCategory> categories) {
		lstCategories.setItems(categories);
		if (catalog != null) {
			lstCategories.setValue(catalog.getCategory());
		}

	}

}
