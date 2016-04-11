package com.duggan.workflow.shared.model.catalog;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.Listable;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.SerializableObj;

public class Catalog extends SerializableObj implements IsCatalogItem, Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private int recordCount=0;
	private String process;
	private List<CatalogColumn> columns = new ArrayList<CatalogColumn>();
	private CatalogType type = CatalogType.DATATABLE;
	private FieldSource fieldSource = FieldSource.FORM;// Field Source
	private String gridName;
	private Long processDefId;
	private ProcessCategory category;

	public Catalog() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toLowerCase();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public List<CatalogColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<CatalogColumn> columns) {
		this.columns = columns;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
	public CatalogType getType() {
		return type;
	}

	public void setType(CatalogType type) {
		this.type = type;
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}

	public FieldSource getFieldSource() {
		return fieldSource;
	}

	public void setFieldSource(FieldSource fieldSource) {
		this.fieldSource = fieldSource;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	@Override
	public String getDisplayName() {
		return description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		return name.equals(((Catalog)(obj)).name);
	}

	public ProcessCategory getCategory() {
		return category;
	}

	public void setCategory(ProcessCategory category) {
		this.category = category;
	}
}
