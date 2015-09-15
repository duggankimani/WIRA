package com.duggan.workflow.client.ui.admin.datatable;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.formbuilder.upload.ImportView;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.events.EditCatalogSchemaEvent;
import com.duggan.workflow.client.ui.grid.AggregationGrid;
import com.duggan.workflow.client.ui.grid.ColumnConfig;
import com.duggan.workflow.client.ui.grid.DataMapper;
import com.duggan.workflow.client.ui.grid.DataModel;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
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

	public CreateTableView() {
		initWidget(uiBinder.createAndBindUi(this));

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
				DataType.INTEGER, "", "input-small"));
		columnConfigs.add(new ColumnConfig("fieldNullable", "Nullable",
				DataType.BOOLEAN));
		columnConfigs.add(new ColumnConfig("fieldPrimary", "Primary Key",
				DataType.BOOLEAN));
		columnConfigs.add(new ColumnConfig("fieldAutoInc", "Auto Increment",
				DataType.BOOLEAN));
		grid.setColumnConfigs(columnConfigs);

		aNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.addRowData(new DataModel());
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
									catalog.setColumns(setLines(uploadedItems.toString()));
									EditCatalogSchemaEvent event = 
											new EditCatalogSchemaEvent(catalog, false);
									AppContext.fireEvent(event);
								} else {
									view.cancelImport();
									EditCatalogSchemaEvent event = new EditCatalogSchemaEvent(getCatalog(), false);
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
	

	private List<CatalogColumn> setLines(String uploadedCSVItems) {
		if(uploadedCSVItems==null || uploadedCSVItems.trim().isEmpty()){
			return new ArrayList<CatalogColumn>();
		}
		
		List<CatalogColumn> lines = new ArrayList<CatalogColumn>();
		
		String[] items = uploadedCSVItems.split("\n");
		for(String item: items){
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
		for(int i=0; i<lineValues.length; i++){
			String value = lineValues[i];
			if(value!=null && !value.isEmpty()){
				//remove quotes, and further trim to take care of values like "" 24"
				value = value.replaceAll("\"", "");
				value = value.trim();
			}
			
			
			if(i==0){
				col.setName(value);
			}else if(i==1){
				col.setLabel(value);
			}else if(i==2){
				try{
					col.setType(DBType.valueOf(value));
				}catch(Exception e){
				}
			}else if (i==3){
				try{
					//Col Size
					col.setSize(Integer.parseInt(value));
				}catch(Exception e){}
				
			}
			
		}
		
		return col;
	}

	public CreateTableView(Catalog catalog) {
		this();

		if (catalog != null) {
			setCatalog(catalog);
			id = catalog.getId();
		}
	}

	protected boolean isNullOrEmpty(String name) {
		return name == null || name.trim().isEmpty();
	}

	public Catalog getCatalog() {
		Catalog catalog = new Catalog();
		catalog.setId(id);
		String name = txtName.getValue().toUpperCase();
		catalog.setName(name.replaceAll("\\s", "")); //Clear empty space
		
		catalog.setDescription(txtDescription.getValue());
		List<CatalogColumn> columns = grid.getData(mapper);
		
		catalog.setColumns(columns);
		return catalog;
	}

	public void setCatalog(Catalog c) {

		if (c.getRecordCount() > 0) {
			spnWarning.addClassName("label label-warning");
			spnWarning.setInnerText("Editing this table will lose all your existing data. Consider exporting your data first.");
		}

		txtName.setValue(c.getName());
		txtDescription.setValue(c.getDescription());
		
		StringBuffer val = new StringBuffer();
		grid.setData(mapper.getDataModels(c.getColumns()));
		List<CatalogColumn> cols = grid.getData(mapper); 
		for(CatalogColumn col: cols){
			val.append(col.getName()+"<br/>");
		}
	}

	public boolean isValid() {
		issues.clear();
		boolean isValid = true;

		if (txtName.getValue().trim().isEmpty()) {
			issues.addError("Table Name is mandatory");
			isValid = false;
		}

		if (getCatalog().getColumns().isEmpty()) {
			issues.addError("Cannot create table with zero columns. "
					+ "Ensure you specify Name, Label and Data Type for each column");
			isValid = false;
		}

		return isValid;
	}

}
