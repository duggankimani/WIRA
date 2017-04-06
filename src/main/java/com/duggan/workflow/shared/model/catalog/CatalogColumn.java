package com.duggan.workflow.shared.model.catalog;

import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.form.Field;
import com.wira.commons.shared.models.SerializableObj;

public class CatalogColumn extends SerializableObj implements IsCatalogItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static final String REFID = "refId";

	public static final String LABEL = "label";

	public static final String NAME = "name";

	public static final String SIZE = "size";

	public static final String TYPE = "type";

	public static final String AUTOINCREMENT = "isautoincrement";

	public static final String NULLABLE = "isnullable";

	public static final String PRIMARYKEY = "isprimarykey";
	
	
	private Long id;
	private String name;
	private String label;
	private DBType type;
	private Integer size;
	private boolean isNullable;
	private boolean isPrimaryKey;
	private boolean isAutoIncrement;
	
	public CatalogColumn() {
	}

	public String getName() {
		String newName = name.replaceAll("\\s", "_");
		return newName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DBType getType() {
		return type;
	}

	public void setType(DBType type) {
		this.type = type;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(name==null || ((CatalogColumn)obj).name==null){
			return false;
		}
		
		return name.equals(((CatalogColumn)obj).name);
	}

	public Field toFormField() {
		Field field = new Field();
		field.setName(name);
		field.setCaption(label);
		field.setType(getType().getFieldType());
		field.setFormId(System.currentTimeMillis());
		return field;
	}
}
