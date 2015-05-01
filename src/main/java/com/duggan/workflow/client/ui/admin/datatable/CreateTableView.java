package com.duggan.workflow.client.ui.admin.datatable;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.grid.AggregationGrid;
import com.duggan.workflow.client.ui.grid.ColumnConfig;
import com.duggan.workflow.client.ui.grid.DataMapper;
import com.duggan.workflow.client.ui.grid.DataModel;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.DataType;
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

public class CreateTableView extends Composite{

	private static CreateTableViewUiBinder uiBinder = GWT
			.create(CreateTableViewUiBinder.class);

	interface CreateTableViewUiBinder extends UiBinder<Widget, CreateTableView> {
	}

	@UiField TextField txtName;
	@UiField TextArea txtDescription;
	@UiField AggregationGrid grid;
	@UiField HasClickHandlers aNew;
	@UiField IssuesPanel issues;
	
	DataMapper mapper = new DataMapper() {
		
		@Override
		public List<DataModel> getDataModels(List objs) {
			List<DataModel> models = new ArrayList<DataModel>();
			for(Object o: objs){
				CatalogColumn c = (CatalogColumn)o;
				DataModel model =  new DataModel();
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
			col.setAutoIncrement(model.get("fieldAutoInc")==null?false:
					(Boolean)model.get("fieldAutoInc"));
			col.setLabel((String)model.get("fieldLabel"));
			col.setName((String)model.get("fieldName"));
			col.setNullable(model.get("fieldNullable")==null?false:
				(Boolean)model.get("fieldNullable"));
			col.setPrimaryKey((Boolean)model.get("fieldPrimary"));
			col.setSize((Integer)model.get("fieldSize"));
			col.setType((DBType)model.get("fieldType"));
			
			if(isNullOrEmpty(col.getName()) || isNullOrEmpty(col.getLabel()) ||
					col.getType()==null){
				return null;
			}
			return col;
		}
	};
	private Long id;
	
	public CreateTableView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		List<ColumnConfig> columnConfigs= new ArrayList<ColumnConfig>();
		columnConfigs.add(new ColumnConfig("fieldName", "Field Name", DataType.STRING,"","input-medium"));
		columnConfigs.add(new ColumnConfig("fieldLabel", "Field Label", DataType.STRING,"","input-medium"));
		
		ColumnConfig config = new ColumnConfig("fieldType", "Type", DataType.SELECTBASIC,"","input-medium");
		config.setDropDownItems(DBType.getValues());
		columnConfigs.add(config);
		
		columnConfigs.add(new ColumnConfig("fieldSize", "Size", DataType.INTEGER,"","input-small"));
		columnConfigs.add(new ColumnConfig("fieldNullable", "Nullable", DataType.BOOLEAN));
		columnConfigs.add(new ColumnConfig("fieldPrimary", "Primary Key", DataType.BOOLEAN));
		columnConfigs.add(new ColumnConfig("fieldAutoInc", "Auto Increment", DataType.BOOLEAN));
		grid.setColumnConfigs(columnConfigs);
		
		aNew.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				grid.addRowData(new DataModel());
			}
		});
	}
	
	
	public CreateTableView(Catalog catalog) {
		this();
		
		if(catalog!=null){
			setCatalog(catalog);
			id = catalog.getId();
		}
	}


	protected boolean isNullOrEmpty(String name) {
		return name==null || name.trim().isEmpty();
	}

	public Catalog getCatalog(){
		Catalog catalog = new Catalog();
		catalog.setId(id);
		catalog.setName(txtName.getValue().toUpperCase());
		catalog.setDescription(txtDescription.getValue());
		List<CatalogColumn> columns = grid.getData(mapper);
		catalog.setColumns(columns);
		return catalog;
	}
	
	public void setCatalog(Catalog c){
		txtName.setValue(c.getName());
		txtDescription.setValue(c.getDescription());
		grid.setData(mapper.getDataModels(c.getColumns()));
	}


	public boolean isValid() {
		issues.clear();
		boolean isValid= true;
		
		if(txtName.getValue().trim().isEmpty()){
			issues.addError("Table Name is mandatory");
			isValid=false;
		}
		
		if(getCatalog().getColumns().isEmpty()){
			issues.addError("Cannot create table with zero columns. "
					+ "Ensure you specify Name, Label and Data Type for each column");
			isValid=false;
		}
		
		return isValid;
	}
	
	

}
